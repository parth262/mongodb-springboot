package com.example.mongospringboottest.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DataRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public Long getTotalCount(String collection) {
        return mongoTemplate.getCollection(collection).countDocuments();
    }

    public Map<String, String> getSchema(String collection) {
        Document datum = mongoTemplate.getCollection(collection).find().first();
        Map<String, String> schema = new HashMap<>();
        datum.entrySet().forEach(entry -> {
            String field = entry.getKey();
            String dataType = entry.getValue().getClass().getSimpleName();
            schema.put(field, dataType);
        });
        return schema;
    }

    public List<Document> query(String collection, Query query) {
        return mongoTemplate.find(query, Document.class, collection);
    }

    public List<Document> aggregation(String collection, Aggregation aggregation) {
        return mongoTemplate.aggregate(aggregation, collection, Document.class).getMappedResults();
    }

}
