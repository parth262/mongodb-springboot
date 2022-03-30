package com.example.mongospringboottest.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.mongospringboottest.dataModel.request.query.QueryRequest;
import com.example.mongospringboottest.dataModel.response.DataResponse;
import com.example.mongospringboottest.dataModel.response.ErrorResponse;
import com.example.mongospringboottest.service.DataService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;
    
    @GetMapping("/{entity}/{id}")
    public Mono<Document> getEntityById (
        @PathVariable(required = true) String entity,
        @PathVariable(required = true) String id
    ) {
        return dataService.getEntityById(entity, id);
    }
    
    @PostMapping("/{entity}/search")
    public Mono<DataResponse> queryData (
        @PathVariable(required = true) String entity,
        @Valid @RequestBody QueryRequest queryRequest
    ) {
        return dataService.search(entity, queryRequest);
    }

    /*
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
    */

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
