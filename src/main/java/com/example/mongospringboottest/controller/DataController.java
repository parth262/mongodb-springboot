package com.example.mongospringboottest.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.mongospringboottest.domain.request.query.QueryRequest;
import com.example.mongospringboottest.domain.response.ErrorResponse;
import com.example.mongospringboottest.domain.response.DataResponse;
import com.example.mongospringboottest.service.DataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;
    
    @GetMapping("/query")
    public ResponseEntity<DataResponse> queryData (
        @RequestParam(required = true) String entity,
        @RequestParam Long skip,
        @RequestParam Integer limit
    ) {
        DataResponse response = dataService.query(entity, skip, limit);
        return ResponseEntity.ok().body(response);
    }
    
    @PostMapping("/query")
    public ResponseEntity<DataResponse> queryData (
        @Valid @RequestBody QueryRequest queryRequest
    ) {
        DataResponse response = dataService.query(queryRequest);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleBaseException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors()
            .stream()
            .map(error -> {
                String fieldName = ((FieldError) error).getField();
                return String.format("%s: %s", fieldName, error.getDefaultMessage());
            }).collect(Collectors.joining(",\n"));
        return new ErrorResponse(errorMessage);
    }

}
