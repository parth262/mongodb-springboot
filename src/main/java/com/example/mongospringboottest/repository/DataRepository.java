package com.example.mongospringboottest.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DataRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;


    public List<Document> getAllDocuments(String table, Integer skip, Integer limit) {
        return mongoTemplate.getCollection(table).find()
            .skip(skip)
            .limit(limit)
            .map(document -> {
                document.remove("_id");
                return document;
            }).into(new ArrayList<>());
    }

    public Long getTotalCount(String table) {
        return mongoTemplate.getCollection(table).countDocuments();
    }
}
