package com.example.mongospringboottest.dataModel.request.query.filters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CompoundFilter extends FilterRequest {

    private List<FilterRequest> filters;
    private LogicalOperator operator;

    public CompoundFilter() {
        super(FilterType.COMPOUND);
    }

    public List<FilterRequest> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterRequest> filters) {
        this.filters = filters;
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public void setOperator(LogicalOperator operator) {
        this.operator = operator;
    }

    @JsonIgnore
    Map<String, List<Object>> getAllFieldValueMap() {
        return filters.stream().flatMap(filter -> filter.getFieldValueMap().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }    
}
