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

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataService {

    @Value("${app.download.records.default-per-request}")
    private Integer DEFAULT_PAGE_SIZE;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private EntityDetailsRepository entityDetailsRepository;

    public Mono<Document> getEntityById(String entity, String id) {
        EntityDetails entityDetails = entityDetailsRepository.getEntityDetails(entity);
        String collection = entityDetails.getTable();
        String idColumn = entityDetails.getIdColumn();
        QueryRequest queryRequest = buildQueryRequestToGetEntityById(idColumn, id);

        Mono<Map<String, String>> schemaMono = dataRepository.getSchema(collection);
        
        return schemaMono.flatMap(schema -> {
            Aggregation aggregation = buildAggregation(queryRequest, entityDetails, schema);
            Flux<Document> documents = dataRepository.aggregation(collection, aggregation);
            return documents.next();
        });
    }

    public QueryRequest buildQueryRequestToGetEntityById(String idColumn, String id) {
        QueryRequest queryRequest = new QueryRequest();
        ComparisonFilter comparisonFilter = new ComparisonFilter(idColumn, ComparisonOperator.EQ, id);
        queryRequest.setFilterRequest(comparisonFilter);
        return queryRequest;
    }

    public Mono<DataResponse> search(String entity, QueryRequest queryRequest) {
        EntityDetails entityDetails = entityDetailsRepository.getEntityDetails(entity);
        String collectionName = entityDetails.getTable();
        Mono<Map<String, String>> schemaMono = dataRepository.getSchema(collectionName);
        
        return schemaMono.flatMap(schema -> {
            Aggregation aggregation = buildAggregation(queryRequest, entityDetails, schema);
            Mono<List<Document>> resultsMono = dataRepository.aggregation(collectionName, aggregation).collectList();
            Mono<Long> countMono = getCount(collectionName, queryRequest.getCount());
            return countMono.zipWith(resultsMono, (count, results) -> {
                DataResponse response = new DataResponse(results);
                response.setCount(count);
                return response;
            }).switchIfEmpty(resultsMono.map(DataResponse::new));
        });
    }

    private Mono<Long> getCount(String collectionName, Boolean fetchCount) {
        if(fetchCount) {
            return dataRepository.getTotalCount(collectionName);
        }
        return Mono.empty();
    }
    
    /*
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
    */

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
