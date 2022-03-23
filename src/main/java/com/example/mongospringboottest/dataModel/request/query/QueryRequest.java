package com.example.mongospringboottest.dataModel.request.query;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryRequest {

    @NotBlank(message = "entity is required.")
    public String entity;

    @JsonProperty(value = "$columns")
    public List<String> columns = Collections.emptyList();

    @NotNull(message = "$paging is required")
    @JsonProperty(value = "$paging")
    @Valid
    public PagingRequest paging;

    @JsonProperty(value = "$sorting")
    public List<@Valid SortRequest> sorting = Collections.emptyList();

    @JsonProperty(value = "$count")
    public Boolean count = false;    

}