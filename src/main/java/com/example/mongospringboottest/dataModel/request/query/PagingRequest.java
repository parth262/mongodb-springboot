package com.example.mongospringboottest.dataModel.request.query;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagingRequest {

    @NotNull(message = "$paging.page is required")
    @Min(value = 0, message = "$paging.page starts from 0")
    @JsonProperty(value = "page")
    public Integer pageNumber;
    
    @NotNull(message = "$paging.size is required")
    @Min(value = 1, message = "$paging.size should be atleast 1")
    @JsonProperty(value = "size")
    public Integer pageSize;

}
