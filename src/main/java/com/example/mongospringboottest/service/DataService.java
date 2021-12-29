package com.example.mongospringboottest.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mongospringboottest.domain.request.query.QueryRequest;
import com.example.mongospringboottest.domain.response.MongoDataResponse;
import com.example.mongospringboottest.repository.DataRepository;
import com.example.mongospringboottest.util.MongoQueryBuilder;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public MongoDataResponse query(String table, Long skip, Integer limit) {
        Query query = mongoQueryBuilder.buildQuery(skip, limit);
        List<Document> results = dataRepository.query(table, query);
        return new MongoDataResponse(results);
    }

    public MongoDataResponse query(String table, QueryRequest queryRequest) {
        Query query = mongoQueryBuilder.buildQuery(queryRequest);
        List<Document> results = dataRepository.query(table, query);
        MongoDataResponse response = new MongoDataResponse(results);
        if(queryRequest.count) {
            Long count = dataRepository.getTotalCount(table);
            response.setCount(count);
        }
        return response;

    }
    
    public StreamingResponseBody downloadData(String table) {
        Long totalRecords = dataRepository.getTotalCount(table);
        return response -> {
            Long remainingRecords = totalRecords;
            Integer page = 0;
            while(remainingRecords > 0) {
                Long skip = (long) page * DEFAULT_PAGE_SIZE;
                Integer limit = Math.min(remainingRecords.intValue(), DEFAULT_PAGE_SIZE);
                Query query = mongoQueryBuilder.buildQuery(skip, limit);
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
    
}
