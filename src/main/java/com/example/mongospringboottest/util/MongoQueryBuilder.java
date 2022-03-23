package com.example.mongospringboottest.util;

import com.example.mongospringboottest.dataModel.request.query.*;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoQueryBuilder implements QueryBuilder<Query> {

    @Override
    public Query build(QueryRequest queryRequest) {
        Query query = new Query();
        query.fields().exclude("_id");

        PagingRequest paging = queryRequest.paging;
        Long skip = (long) (paging.pageNumber * paging.pageSize);
        Integer limit = paging.pageSize;

        query.skip(skip).limit(limit);
        
        if(!queryRequest.columns.isEmpty()) {
            query.fields().include(queryRequest.columns.toArray(new String[0]));
        }
        
        queryRequest.sorting.forEach(sort -> {
            Sort.Direction sortDirection = sort.isAscending ? Sort.Direction.ASC : Sort.Direction.DESC;
            query.with(Sort.by(sortDirection, sort.field));
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
