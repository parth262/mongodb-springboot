package com.example.mongospringboottest.dataModel.request.query.filters;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(allOf = FilterRequest.class)
public abstract class SimpleFilter extends FilterRequest {    
    private String field;

    public SimpleFilter() {
    }

    public SimpleFilter(FilterType type, String field) {
        super(type);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public abstract List<Object> getValues();
    
}
