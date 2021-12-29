package com.example.mongospringboottest.domain.request.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sort {

    public String field;
    @JsonProperty(value = "ascending")
    public Boolean isAscending;

    public Sort() {}

    public Sort(String field, Boolean isAscending) {
        this.field = field;
        this.isAscending = isAscending;
    }
    
}
