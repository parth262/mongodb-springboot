package com.example.mongospringboottest.domain.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sort {

    public String field;
    @JsonProperty(value = "ascending")
    public Boolean isAscending;
    
}
