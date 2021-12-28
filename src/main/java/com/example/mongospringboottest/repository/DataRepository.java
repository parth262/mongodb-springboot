package com.example.mongospringboottest.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DataRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public Long getTotalCount(String table) {
        return mongoTemplate.getCollection(table).countDocuments();
    }

    public List<Document> query(String table, Query query) {
        return mongoTemplate.find(query, Document.class, table);
    }

}
