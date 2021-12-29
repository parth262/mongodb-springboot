package com.example.mongospringboottest.domain.response;

import java.util.List;

public class CollectionResponse<T> {

    private List<T> results;

    public CollectionResponse() {}

    public CollectionResponse(List<T> results) {
        this.results = results;
    }

    public List<T> getResults() {
        return this.results;
    }
    
}
