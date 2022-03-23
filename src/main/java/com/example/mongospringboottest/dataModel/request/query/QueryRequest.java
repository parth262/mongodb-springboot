package com.example.mongospringboottest.dataModel.request.query;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.mongospringboottest.dataModel.request.query.filters.FilterRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Query")
public class QueryRequest {

    private Boolean count = false;

    private List<String> columns = Collections.emptyList();

    @NotNull(message = "$paging is required")
    @JsonProperty(value = "paging")
    @Valid
    private PagingRequest pagingRequest;

    @JsonProperty(value = "sorting")
    @Valid
    private List<@Valid SortRequest> sortRequests = Collections.emptyList();

    @JsonProperty(value = "filter")
    private FilterRequest filterRequest;

    public Boolean getCount() {
        return count;
    }

    public void setCount(Boolean count) {
        this.count = count;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public PagingRequest getPagingRequest() {
        return pagingRequest;
    }

    public void setPagingRequest(PagingRequest pagingRequest) {
        this.pagingRequest = pagingRequest;
    }

    public List<SortRequest> getSortRequests() {
        return sortRequests;
    }

    public void setSortRequests(List<SortRequest> sortRequests) {
        this.sortRequests = sortRequests;
    }

    public FilterRequest getFilterRequest() {
        return filterRequest;
    }

    public void setFilterRequest(FilterRequest filterRequest) {
        this.filterRequest = filterRequest;
    }

}