package com.example.mongospringboottest.controller;

import java.io.FileNotFoundException;
import java.util.List;

import com.example.mongospringboottest.domain.query.Query;
import com.example.mongospringboottest.service.DataService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;
    
    @GetMapping("/query")
    public List<Document> queryData (
        @RequestParam String table,
        @RequestParam Integer skip,
        @RequestParam Integer limit
    ) {
        return dataService.query(table, skip, limit);
    }
    
    @PostMapping("/query")
    public List<Document> queryData (
        @RequestParam String table,
        @RequestBody Query query
    ) {
        return dataService.query(table, query);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadAllDocuments(
        @RequestParam String table) throws FileNotFoundException {
        StreamingResponseBody stream = dataService.downloadData(table);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=download.csv")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(stream);
    }

}
