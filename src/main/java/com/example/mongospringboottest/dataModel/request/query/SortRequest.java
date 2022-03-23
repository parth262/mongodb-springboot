package com.example.mongospringboottest.dataModel.request.query;

import javax.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Sort")
public class SortRequest {

    @NotBlank(message = "sorting.field is required")
    private String field;
    
    private Boolean ascending = true;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Boolean getAscending() {
        return ascending;
    }

    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }
    
}
