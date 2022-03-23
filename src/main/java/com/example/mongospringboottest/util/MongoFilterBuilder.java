package com.example.mongospringboottest.util;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mongospringboottest.dataModel.request.query.filters.BetweenFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.ComparisonFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.CompoundFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.FilterRequest;
import com.example.mongospringboottest.dataModel.request.query.filters.INFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.SimpleFilter;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class MongoFilterBuilder {

    public static MatchOperation build(FilterRequest filterRequest) {
        return Aggregation.match(buildFilter(filterRequest));
    }

    private static Criteria buildFilter(
        FilterRequest filterRequest
    ) {
        Criteria criteria = new Criteria();
        switch(filterRequest.getType()) {
            case COMPARISON:
            case BETWEEN:
            case IN:
                SimpleFilter simpleFilter = (SimpleFilter) filterRequest;
                criteria = buildSimpleFilter(simpleFilter);
                break;
            case COMPOUND:
                CompoundFilter compoundFilter = (CompoundFilter) filterRequest;
                criteria = buildCompoundFilter(compoundFilter);
                break;
        }

        if(filterRequest.getNegate()) {
            criteria.not();
        }
        return criteria;
    }

    private static Criteria buildSimpleFilter(SimpleFilter simpleFilter) {
        switch(simpleFilter.getType()) {
            case BETWEEN:
                BetweenFilter betweenFilter = (BetweenFilter) simpleFilter;
                return buildBetweenFilter(betweenFilter);
            case COMPARISON:
                ComparisonFilter comparisonFilter = (ComparisonFilter) simpleFilter;
                return buildComparisonFilter(comparisonFilter);
            case IN:
                INFilter inFilter = (INFilter) simpleFilter;
                return buildINFilter(inFilter);
            default:
                return new Criteria();
        }
    }

    private static Criteria buildComparisonFilter(ComparisonFilter comparisonFilter) {
        Object value = comparisonFilter.getValue();
        Criteria criteria = Criteria.where(comparisonFilter.getField());
        switch(comparisonFilter.getCondition()) {
            case EQ:
                criteria.is(value);
                break;
            case GE:
                criteria.gte(value);
                break;
            case GT:
                criteria.gt(value);
                break;
            case LE:
                criteria.lte(value);
                break;
            case LIKE:
                criteria.regex(value.toString());
                break;
            case LT:
                criteria.lt(value);
                break;
            case NE:
                criteria.ne(value);
                break;
        }
        return criteria;
    }

    private static Criteria buildBetweenFilter(BetweenFilter betweenFilter) {
        return Criteria.where(betweenFilter.getField())
                .gte(betweenFilter.getFrom())
                .lte(betweenFilter.getTo());
    }

    private static Criteria buildINFilter(INFilter inFilter) {
        return Criteria.where(inFilter.getField())
                .in(inFilter.getValues().toArray());
    }

    private static Criteria buildCompoundFilter(CompoundFilter compoundFilter) {

        Criteria criteria = new Criteria();

        List<Criteria> criterias = compoundFilter.getFilters().stream()
            .map(MongoFilterBuilder::buildFilter)
            .collect(Collectors.toList());

        switch(compoundFilter.getOperator()) {
            case AND:
                criteria.andOperator(criterias);
                break;
            case OR:
                criteria.orOperator(criterias);
                break;
        }

        return criteria;
    }
    
}
