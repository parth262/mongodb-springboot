package com.example.mongospringboottest.domain.request.query;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryRequest {

    @JsonProperty(required = true)
    public String entity;
    @JsonProperty(value = "$columns")
    public List<String> columns = Collections.emptyList();
    @JsonProperty(value = "$paging")
    public PagingRequest paging = new PagingRequest();
    @JsonProperty(value = "$sorting")
    public List<SortRequest> sorting = Collections.emptyList();
    @JsonProperty(value = "$count")
    public Boolean count = false;    

}