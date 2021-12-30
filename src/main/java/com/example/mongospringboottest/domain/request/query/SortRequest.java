package com.example.mongospringboottest.domain.request.query;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SortRequest {

    @NotBlank(message = "$sorting.field is required")
    public String field;
    
    @JsonProperty(value = "ascending")
    public Boolean isAscending = true;
    
}
