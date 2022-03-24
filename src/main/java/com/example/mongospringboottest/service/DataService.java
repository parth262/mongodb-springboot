package com.example.mongospringboottest.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mongospringboottest.dataModel.ColumnEntityMapping;
import com.example.mongospringboottest.dataModel.EntityDetails;
import com.example.mongospringboottest.dataModel.request.query.QueryRequest;
import com.example.mongospringboottest.dataModel.request.query.filters.ComparisonFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.ComparisonOperator;
import com.example.mongospringboottest.dataModel.response.DataResponse;
import com.example.mongospringboottest.repository.DataRepository;
import com.example.mongospringboottest.util.EntityDetailsRepository;
import com.example.mongospringboottest.util.MongoAggregationBuilder;
import com.example.mongospringboottest.util.MongoQueryBuilder;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class DataService {

    @Value("${app.download.records.default-per-request}")
    private Integer DEFAULT_PAGE_SIZE;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MongoQueryBuilder mongoQueryBuilder;

    @Autowired
    private EntityDetailsRepository entityDetailsRepository;

    public Document getEntityById(String entity, String id) {
        EntityDetails entityDetails = entityDetailsRepository.getEntityDetails(entity);
        String collection = entityDetails.getTable();
        String idColumn = entityDetails.getIdColumn();
        Map<String, String> schema = dataRepository.getSchema(collection);
        QueryRequest queryRequest = buildQueryRequestToGetEntityById(idColumn, id);
        Aggregation aggregation = buildAggregation(queryRequest, entityDetails, schema);
        List<Document> documents = dataRepository.aggregation(collection, aggregation);
        if(documents.isEmpty()) {
            return new Document();
        }
        return documents.get(0);
    }

    public QueryRequest buildQueryRequestToGetEntityById(String idColumn, String id) {
        QueryRequest queryRequest = new QueryRequest();
        ComparisonFilter comparisonFilter = new ComparisonFilter(idColumn, ComparisonOperator.EQ, id);
        queryRequest.setFilterRequest(comparisonFilter);
        return queryRequest;
    }

    public DataResponse search(String entity, QueryRequest queryRequest) {
        EntityDetails entityDetails = entityDetailsRepository.getEntityDetails(entity);
        String collection = entityDetails.getTable();
        Map<String, String> schema = dataRepository.getSchema(collection);
        Aggregation aggregation = buildAggregation(queryRequest, entityDetails, schema);
        List<Document> results = dataRepository.aggregation(collection, aggregation);
        DataResponse response = new DataResponse(results);
        if(queryRequest.getCount()) {
            Long count = dataRepository.getTotalCount(collection);
            response.setCount(count);
        }
        return response;
    }
    
    public StreamingResponseBody downloadData(String entity) {
        String table = entityDetailsRepository.getTable(entity);
        Long totalRecords = dataRepository.getTotalCount(table);
        return response -> {
            Long remainingRecords = totalRecords;
            Integer page = 0;
            while(remainingRecords > 0) {
                Long skip = (long) page * DEFAULT_PAGE_SIZE;
                Integer limit = Math.min(remainingRecords.intValue(), DEFAULT_PAGE_SIZE);
                Query query = mongoQueryBuilder.build(skip, limit);
                List<Document> documents = dataRepository.query(table, query);
                if(page == 0) {
                    String header = getHeaderFromDocument(documents.get(0));
                    response.write((header + "\n").getBytes());
                }
                String data = documents.stream()
                    .map(this::getCSVOfDocuments)
                    .collect(Collectors.joining("\n"));
                response.write((data + "\n").getBytes());
                response.flush();
                remainingRecords -= documents.size();
                page++;
            }
        };
    }

    private String getCSVOfDocuments(Document document) {
        return document.values()
            .stream()
            .map(Object::toString)
            .collect(Collectors.joining(","));
    }

    private String getHeaderFromDocument(Document document) {
        return document.keySet()
            .stream()
            .collect(Collectors.joining(","));
    }

    private Aggregation buildAggregation(
        QueryRequest queryRequest, 
        EntityDetails entityDetails,
        Map<String, String> schema
    ) {
        List<ColumnEntityMapping> filteredColumnEntityMappings = filterColumnEntityMappings(
            entityDetails.getColumnEntityMappings(), 
            queryRequest.getColumns()
        );

        return new MongoAggregationBuilder(entityDetailsRepository)
            .setColumns(queryRequest.getColumns())
            .setColumnEntityMappings(filteredColumnEntityMappings)
            .setFilter(queryRequest.getFilterRequest(), schema)
            .setSorts(queryRequest.getSortRequests())
            .setPagination(queryRequest.getPagingRequest())
            .build();
    }

    private List<ColumnEntityMapping> filterColumnEntityMappings(
        List<ColumnEntityMapping> columnEntityMappings,
        List<String> requestedColumns
    ) {
        if(requestedColumns.isEmpty()) {
            return columnEntityMappings;
        }

        return columnEntityMappings.stream()
            .filter(columnEntityMapping -> requestedColumns.contains(columnEntityMapping.getNewField()))
            .collect(Collectors.toList());
    }
}
