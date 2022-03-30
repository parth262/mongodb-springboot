package com.example.mongospringboottest.repository;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DataRepository {
    
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public Mono<Long> getTotalCount(String collectionName) {
        return mongoTemplate.count(new Query(), collectionName);
    }

    public Mono<Map<String, String>> getSchema(String collectionName) {
        return mongoTemplate.find(new Query(), Document.class, collectionName)
            .next()
            .map(document -> {
                Map<String, String> schema = new HashMap<>();
                document.entrySet().forEach(entry -> {
                    String field = entry.getKey();
                    String dataType = entry.getValue().getClass().getSimpleName();
                    schema.put(field, dataType);
                });
                return schema;
            });
    }

    public Flux<Document> query(String collectionName, Query query) {
        return mongoTemplate.find(query, Document.class, collectionName);
    }

    public Flux<Document> aggregation(String collectionName, Aggregation aggregation) {
        return mongoTemplate.aggregate(aggregation, collectionName, Document.class);
    }

}
