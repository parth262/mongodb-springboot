package com.example.mongospringboottest.domain.query;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Query {

    @JsonProperty(value = "$paging")
    public Paging paging;
    @JsonProperty(value = "$sorting")
    public List<Sort> sorting = Collections.emptyList();
    public Boolean count = false;

    public Query() {
        this.paging = new Paging();
    }

    public Query(Paging paging) {
        this.paging = paging;
    }

    public Query(Paging paging, List<Sort> sorting, Boolean count) {
        this.paging = paging;
        this.sorting = sorting;
        this.count = count;
    }

}