package com.example.mongospringboottest.domain.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.bson.Document;

@JsonInclude(Include.NON_NULL)
public class MongoDataResponse extends CollectionResponse<Document> {
    private Long count;

    public MongoDataResponse() {}

    public MongoDataResponse(List<Document> results) {
        super(results);
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
    
}
