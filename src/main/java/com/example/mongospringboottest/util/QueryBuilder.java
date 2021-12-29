package com.example.mongospringboottest.util;

import com.example.mongospringboottest.domain.request.query.QueryRequest;

public interface QueryBuilder<T> {
    
    public T buildQuery(QueryRequest queryRequest);

}
