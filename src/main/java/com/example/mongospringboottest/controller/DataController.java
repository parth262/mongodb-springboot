package com.example.mongospringboottest.controller;

import com.example.mongospringboottest.domain.request.query.QueryRequest;
import com.example.mongospringboottest.domain.response.ErrorResponse;
import com.example.mongospringboottest.domain.response.MongoDataResponse;
import com.example.mongospringboottest.service.DataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<MongoDataResponse> queryData (
        @RequestParam String table,
        @RequestParam Long skip,
        @RequestParam Integer limit
    ) {
        MongoDataResponse response = dataService.query(table, skip, limit);
        return ResponseEntity.ok().body(response);
    }
    
    @PostMapping("/query")
    public ResponseEntity<MongoDataResponse> queryData (
        @RequestParam String table,
        @RequestBody QueryRequest query
    ) {
        MongoDataResponse response = dataService.query(table, query);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadAllDocuments(
        @RequestParam String table
    ) {
        StreamingResponseBody stream = dataService.downloadData(table);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=download.csv")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(stream);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleBaseException(Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

}
