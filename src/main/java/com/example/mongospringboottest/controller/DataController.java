package com.example.mongospringboottest.controller;

import java.util.List;

import com.example.mongospringboottest.service.DataService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;
    
    @GetMapping("/data")
    public List<Document> getAllDocuments(
        @RequestParam String table,
        @RequestParam Integer skip,
        @RequestParam Integer limit
    ) {
        return dataService.findAllDocuments(table, skip, limit);
    }

}
