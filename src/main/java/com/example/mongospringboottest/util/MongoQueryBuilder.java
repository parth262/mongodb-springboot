package com.example.mongospringboottest.util;

import com.example.mongospringboottest.dataModel.request.query.*;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoQueryBuilder {    

    public Query build(QueryRequest queryRequest) {
        Query query = new Query();
        query.fields().exclude("_id");

        PagingRequest paging = queryRequest.getPagingRequest();
        Long skip = (long) (paging.getPage() * paging.getSize());
        Integer limit = paging.getSize();

        query.skip(skip).limit(limit);
        
        if(!queryRequest.getColumns().isEmpty()) {
            query.fields().include(queryRequest.getColumns().toArray(new String[0]));
        }
        
        queryRequest.getSortRequests().forEach(sort -> {
            Sort.Direction sortDirection = sort.getAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
            query.with(Sort.by(sortDirection, sort.getField()));
        });

        return query;
    }

    public Query build(Long skip, Integer limit) {
        Query query = new Query();
        query.fields().exclude("_id");
        query.skip(skip).limit(limit);
        return query;
    }

}
