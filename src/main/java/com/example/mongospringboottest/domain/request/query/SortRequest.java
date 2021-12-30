package com.example.mongospringboottest.domain.request.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SortRequest {

    public String field;
    @JsonProperty(value = "ascending")
    public Boolean isAscending;
    
}
