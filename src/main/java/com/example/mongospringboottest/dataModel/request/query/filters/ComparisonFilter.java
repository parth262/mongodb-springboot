package com.example.mongospringboottest.dataModel.request.query.filters;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComparisonFilter extends SimpleFilter {
    private ComparisonOperator condition;
    private Object value;

    public ComparisonFilter() {
    }

    public ComparisonFilter(String field, ComparisonOperator condition, Object value) {
        super(FilterType.COMPARISON, field);
        this.condition = condition;
        this.value = value;
    }

    public ComparisonOperator getCondition() {
        return condition;
    }

    public void setCondition(ComparisonOperator condition) {
        this.condition = condition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    @JsonIgnore
    public List<Object> getValues() {
        return Collections.singletonList(value);
    }
    
}
