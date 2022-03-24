package com.example.mongospringboottest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mongospringboottest.dataModel.ColumnEntityMapping;
import com.example.mongospringboottest.dataModel.EntityDetails;
import com.example.mongospringboottest.dataModel.request.query.*;
import com.example.mongospringboottest.dataModel.request.query.filters.FilterRequest;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;

public class MongoAggregationBuilder {
    private List<AggregationOperation> operations;
    private EntityDetailsRepository entityDetailsRepository;

    public MongoAggregationBuilder(EntityDetailsRepository entityDetailsRepository) {
        this.entityDetailsRepository = entityDetailsRepository;
        this.operations = new ArrayList<>();
    }

    public MongoAggregationBuilder setColumns(List<String> columns) {
        ProjectionOperation projectionOperation = Aggregation.project().andExclude("_id");
        if(!columns.isEmpty()) {
            projectionOperation = projectionOperation.andInclude(columns.toArray(new String[0]));
        }
        this.operations.add(projectionOperation);
        return this;
    }

    public MongoAggregationBuilder setColumnEntityMappings(List<ColumnEntityMapping> columnEntityMappings) {
        List<LookupOperation> lookupOperations = columnEntityMappings.stream()
            .map(columnEntityMapping -> {
                EntityDetails entityDetails = entityDetailsRepository.getEntityDetails(columnEntityMapping.getEntity());
                String from = entityDetails.getTable();
                String localField = columnEntityMapping.getSourceField();
                String foreignField = columnEntityMapping.getForeignField();
                String newField = columnEntityMapping.getNewField();                
                return Aggregation.lookup(from, localField, foreignField, newField);
            }).collect(Collectors.toList());
        this.operations.addAll(lookupOperations);
        return this;
    }

    public MongoAggregationBuilder setFilter(FilterRequest filterRequest, Map<String, String> schema) {
        if(filterRequest != null) {
            MatchOperation matchOperation = MongoFilterBuilder.build(filterRequest, schema);
            this.operations.add(matchOperation);
        }
        return this;
    }

    public MongoAggregationBuilder setSorts(List<SortRequest> sortRequests) {
        List<SortOperation> sortOperations = sortRequests.stream()
            .map(sortRequest -> {
                Sort.Direction sortDirection = sortRequest.getAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                return Aggregation.sort(Sort.by(sortDirection, sortRequest.getField()));
            }).collect(Collectors.toList());
        this.operations.addAll(sortOperations);
        return this;
    }

    public MongoAggregationBuilder setPagination(PagingRequest pagingRequest) {
        if(pagingRequest != null) {
            Long skip = (long) (pagingRequest.getPage() * pagingRequest.getSize());
            SkipOperation skipOperation = Aggregation.skip(skip);
            this.operations.add(skipOperation);

            Integer limit = pagingRequest.getSize();
            LimitOperation limitOperation = Aggregation.limit(limit);
            this.operations.add(limitOperation);
        }
        return this;
    }

    public Aggregation build() {
        return Aggregation.newAggregation(this.operations);
    }    
    
}
