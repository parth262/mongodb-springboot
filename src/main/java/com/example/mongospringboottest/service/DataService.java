package com.example.mongospringboottest.service;

import java.util.List;

import com.example.mongospringboottest.repository.DataRepository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public List<Document> findAllDocuments(String table, Integer skip, Integer limit) {
        Long startTime = System.currentTimeMillis();
        List<Document> result = dataRepository.getAllDocuments(table, skip, limit);
        Long endTime = System.currentTimeMillis();
        System.out.println("Total Time Taken in milliseconds: " + (endTime - startTime));
        System.out.println("Total records fetched: " + result.size());
        return result;
    }
    
}
