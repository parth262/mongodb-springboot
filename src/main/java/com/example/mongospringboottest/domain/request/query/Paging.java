package com.example.mongospringboottest.domain.request.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Paging {

    @JsonProperty(value = "page")
    public Integer pageNumber;
    @JsonProperty(value = "size")
    public Integer pageSize;

    public Paging() {
        this.pageNumber = 0;
        this.pageSize = 50;
    }

    public Paging(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

}
