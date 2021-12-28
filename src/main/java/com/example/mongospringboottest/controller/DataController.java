package com.example.mongospringboottest.controller;

import java.io.FileNotFoundException;
import java.util.List;

import com.example.mongospringboottest.service.DataService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api")
public class DataController {
    
    @Value("${download.records.maxPerRequest}")
    private Integer MAX_RECORDS_PER_REQUEST;

    @Autowired
    private DataService dataService;
    
    @GetMapping("/data")
    public List<Document> getAllDocuments(
        @RequestParam String table,
        @RequestParam Integer skip,
        @RequestParam Integer limit
    ) {
        return dataService.findAllDocuments(table, skip, Math.min(limit, MAX_RECORDS_PER_REQUEST));
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
