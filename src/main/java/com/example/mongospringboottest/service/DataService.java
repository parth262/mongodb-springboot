package com.example.mongospringboottest.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mongospringboottest.domain.query.Paging;
import com.example.mongospringboottest.domain.query.QueryRequest;
import com.example.mongospringboottest.repository.DataRepository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class DataService {

    @Value("${app.download.records.default-per-request}")
    private Long DEFAULT_PAGE_SIZE;

    @Autowired
    private DataRepository dataRepository;

    public List<Document> query(String table, Long skip, Integer limit) {
        Query query = buildMongoQuery(skip, limit);
        List<Document> result = dataRepository.query(table, query);
        return result;
    }

    public List<Document> query(String table, QueryRequest queryRequest) {
        Query query = buildMongoQuery(queryRequest);
        return dataRepository.query(table, query);
    }
    
    public StreamingResponseBody downloadData(String table) {
        Long totalRecords = dataRepository.getTotalCount(table);
        return response -> {
            Long remainingRecords = totalRecords;
            Integer page = 0;
            while(remainingRecords > 0) {
                Long skip = page * DEFAULT_PAGE_SIZE;
                Integer limit = Math.min(remainingRecords.intValue(), DEFAULT_PAGE_SIZE.intValue());
                Query query = buildMongoQuery(skip, limit);
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

    private Query buildMongoQuery(Long skip, Integer limit) {
        Query query = new Query();
        query.fields().exclude("_id");
        query.skip(skip).limit(limit);
        return query;
    }

    private Query buildMongoQuery(QueryRequest queryRequest) {
        Query query = new Query();
        query.fields().exclude("_id");

        Paging paging = queryRequest.paging;
        Long skip = (long) (paging.pageNumber * paging.pageSize);
        Integer limit = paging.pageSize;

        query.skip(skip).limit(limit);
        
        if(!queryRequest.columns.isEmpty()) {
            query.fields().include(queryRequest.columns.toArray(new String[0]));
        }
        
        queryRequest.sorting.forEach(sort -> {
            Sort.Direction sortDirection = sort.isAscending ? Sort.Direction.ASC : Sort.Direction.DESC;
            query.with(Sort.by(sortDirection, sort.field));
        });

        return query;
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
