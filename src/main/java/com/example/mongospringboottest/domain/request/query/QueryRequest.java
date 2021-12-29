package com.example.mongospringboottest.domain.request.query;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryRequest {

    @JsonProperty(value = "$columns")
    public List<String> columns = Collections.emptyList();
    @JsonProperty(value = "$paging")
    public Paging paging;
    @JsonProperty(value = "$sorting")
    public List<Sort> sorting = Collections.emptyList();
    @JsonProperty(value = "$count")
    public Boolean count = false;

    public QueryRequest() {
        this.paging = new Paging();
    }

    public QueryRequest(Paging paging) {
        this.paging = paging;
    }

    public QueryRequest(Paging paging, List<Sort> sorting, Boolean count) {
        this.paging = paging;
        this.sorting = sorting;
        this.count = count;
    }

}