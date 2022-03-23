package com.example.mongospringboottest.dataModel.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.bson.Document;

@JsonInclude(Include.NON_NULL)
public class DataResponse {
    private Long count;

    private List<Document> results;

    public DataResponse() {}

    public DataResponse(List<Document> results) {
        this.results = results;
    }

    public List<Document> getResults() {
        return results;
    }

    public void setResults(List<Document> results) {
        this.results = results;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
    
}
