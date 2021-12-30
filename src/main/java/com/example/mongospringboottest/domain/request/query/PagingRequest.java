package com.example.mongospringboottest.domain.request.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagingRequest {

    @JsonProperty(value = "page")
    public Integer pageNumber;
    @JsonProperty(value = "size")
    public Integer pageSize;

    public PagingRequest() {
        this.pageNumber = 0;
        this.pageSize = 50;
    }

    public PagingRequest(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

}
