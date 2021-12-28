package com.example.mongospringboottest.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mongospringboottest.domain.query.Query;
import com.example.mongospringboottest.repository.DataRepository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class DataService {

    @Value("${app.download.records.default-per-request}")
    private Integer DEFAULT_PAGE_SIZE;

    @Autowired
    private DataRepository dataRepository;

    public List<Document> query(String table, Integer skip, Integer limit) {
        List<Document> result = dataRepository.query(table, skip, limit);
        return result;
    }

    public List<Document> query(String table, Query query) {
        return dataRepository.query(table, query);
    }

    private String getCSVOfDocuments(Document document) {
        return document.values().stream().map(Object::toString).collect(Collectors.joining(","));
    }

    private String getHeaderFromDocument(Document document) {
        return document.keySet().stream().collect(Collectors.joining(","));
    }
    
    public StreamingResponseBody downloadData(String table) {
        Long totalRecords = dataRepository.getTotalCount(table);
        return response -> {
            Long remainingRecords = totalRecords;
            Integer page = 0;
            while(remainingRecords > 0) {
                Integer skip = page * DEFAULT_PAGE_SIZE;
                Integer limit = Math.min(remainingRecords.intValue(), DEFAULT_PAGE_SIZE);                
                List<Document> documents = dataRepository.query(table, skip, limit);
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
    
}
