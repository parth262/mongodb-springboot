package com.example.mongospringboottest.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mongospringboottest.dataModel.EntityDetails;
import com.example.mongospringboottest.dataModel.request.query.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Component;

@Component
public class MongoAggregationBuilder implements QueryBuilder<Aggregation> {

    @Autowired
    private EntityDetailsProvider entityDetailsProvider;

    @Override
    public Aggregation build(QueryRequest queryRequest) {
        List<LookupOperation> lookupOperations = buildLookupOperations(queryRequest.entity, queryRequest.columns);
        ProjectionOperation projectionOperation = buildProjectionOperation(queryRequest.columns);
        List<AggregationOperation> skipLimitOperations = mapPagingRequestToSkipAndLimitOperations(queryRequest.paging);
        List<SortOperation> sortOperations = mapSortRequestsToSortOperations(queryRequest.sorting);

        List<AggregationOperation> operations = new ArrayList<>();
        operations.addAll(sortOperations);
        operations.add(projectionOperation);
        operations.addAll(skipLimitOperations);
        operations.addAll(lookupOperations);

        return Aggregation.newAggregation(operations);
    }

    private List<LookupOperation> buildLookupOperations(String entity, List<String> selectedColumns) {
        EntityDetails entityDetails = entityDetailsProvider.getEntityDetails(entity);
        Map<String, String> columnEntityMap = entityDetails.getColumnEntityMap();
        return columnEntityMap.entrySet().stream()
            .filter(entry -> selectedColumns.isEmpty() || selectedColumns.contains(entry.getKey()))
            .map(entry -> {
                EntityDetails nestedEntityDetail = entityDetailsProvider.getEntityDetails(entry.getValue());
                String from = nestedEntityDetail.getTable();
                String localField = entry.getKey();
                String foreignField = nestedEntityDetail.getIdColumn();
                String as = localField;
                return Aggregation.lookup(from, localField, foreignField, as);
            }).collect(Collectors.toList());
    }

    private ProjectionOperation buildProjectionOperation(List<String> columns) {
        ProjectionOperation projectionOperation = Aggregation.project().andExclude("_id");
        if(!columns.isEmpty()) {
            projectionOperation = projectionOperation.andInclude(columns.toArray(new String[0]));
        }
        return projectionOperation;
    }

    private List<AggregationOperation> mapPagingRequestToSkipAndLimitOperations(PagingRequest pagingRequest) {
        Long skip = (long) (pagingRequest.pageNumber * pagingRequest.pageSize);
        Integer limit = pagingRequest.pageSize;

        SkipOperation skipOperation = Aggregation.skip(skip);
        LimitOperation limitOperation = Aggregation.limit(limit);

        return Arrays.asList(skipOperation, limitOperation);
    }

    private List<SortOperation> mapSortRequestsToSortOperations(List<SortRequest> sortRequests) {
        return sortRequests.stream()
        .map(this::mapSortRequestToSortOperation)
        .collect(Collectors.toList());
    }

    private SortOperation mapSortRequestToSortOperation(SortRequest sortRequest) {
        Sort.Direction sortDirection = sortRequest.isAscending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Aggregation.sort(Sort.by(sortDirection, sortRequest.field));
    }
    
}
