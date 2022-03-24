package com.example.mongospringboottest.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.mongospringboottest.dataModel.request.query.filters.BetweenFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.ComparisonFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.ComparisonOperator;
import com.example.mongospringboottest.dataModel.request.query.filters.CompoundFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.FilterRequest;
import com.example.mongospringboottest.dataModel.request.query.filters.INFilter;
import com.example.mongospringboottest.dataModel.request.query.filters.LogicalOperator;
import com.example.mongospringboottest.dataModel.request.query.filters.SimpleFilter;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class MongoFilterBuilder {

    public static MatchOperation build(FilterRequest filterRequest, Map<String, String> schema) {
        Criteria filterCriteria = buildFilter(filterRequest, schema);
        return Aggregation.match(filterCriteria);
    }

    private static Criteria buildFilter(
        FilterRequest filterRequest,
        Map<String, String> schema
    ) {
        Criteria criteria = new Criteria();
        switch(filterRequest.getType()) {
            case COMPARISON:
            case BETWEEN:
            case IN:
                SimpleFilter simpleFilter = (SimpleFilter) filterRequest;
                String dataType = schema.get(simpleFilter.getField());
                criteria = buildSimpleFilter(simpleFilter, dataType);
                break;
            case COMPOUND:
                CompoundFilter compoundFilter = (CompoundFilter) filterRequest;
                criteria = buildCompoundFilter(compoundFilter, schema);
                break;
        }

        return criteria;
    }

    private static Criteria buildSimpleFilter(SimpleFilter simpleFilter, String dataType) {
        Function<String, Object> conversionFunction = ConversionUtil.getConversionFunction(dataType);
        switch(simpleFilter.getType()) {
            case BETWEEN:
                BetweenFilter betweenFilter = (BetweenFilter) simpleFilter;
                return buildBetweenFilter(betweenFilter, conversionFunction);
            case COMPARISON:
                ComparisonFilter comparisonFilter = (ComparisonFilter) simpleFilter;
                return buildComparisonFilter(comparisonFilter, conversionFunction);
            case IN:
                INFilter inFilter = (INFilter) simpleFilter;
                return buildINFilter(inFilter, conversionFunction);
            default:
                return new Criteria();
        }
    }

    private static Criteria buildComparisonFilter(ComparisonFilter comparisonFilter, Function<String, Object> conversionFunction) {
        Object value = conversionFunction.apply(comparisonFilter.getValue().toString());
        Criteria criteria = Criteria.where(comparisonFilter.getField());
        Boolean negate = comparisonFilter.getNegate();
        Boolean isEqualityComparison = Set.of(ComparisonOperator.EQ, ComparisonOperator.NE).contains(comparisonFilter.getCondition());
        if(negate && !isEqualityComparison) {
            criteria.not();
        }
        switch(comparisonFilter.getCondition()) {
            case EQ:
                if(negate) criteria.ne(value);
                else criteria.is(value);
                break;
            case NE:
                if(negate) criteria.is(value);
                else criteria.ne(value);
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
        }
        return criteria;
    }

    private static Criteria buildBetweenFilter(BetweenFilter betweenFilter, Function<String, Object> conversionFunction) {
        Object from = conversionFunction.apply(betweenFilter.getFrom().toString());
        Object to = conversionFunction.apply(betweenFilter.getTo().toString());
        if(betweenFilter.getNegate()) {
            return Criteria.where(betweenFilter.getField())
                .not()
                .gte(from)
                .lte(to);
        }
        return Criteria.where(betweenFilter.getField())
                .gte(from)
                .lte(to);
    }

    private static Criteria buildINFilter(INFilter inFilter, Function<String, Object> conversionFunction) {
        List<Object> values = inFilter.getValues().stream()
            .map(value -> conversionFunction.apply(value.toString()))
            .collect(Collectors.toList());
        if(inFilter.getNegate()) {
            return Criteria.where(inFilter.getField())
            .nin(values);
        }
        return Criteria.where(inFilter.getField())
            .in(values);
    }

    private static Criteria buildCompoundFilter(CompoundFilter compoundFilter, Map<String, String> schema) {

        Criteria criteria = new Criteria();

        List<Criteria> criterias = compoundFilter.getFilters().stream()
            .map(filter -> {
                filter.setNegate(filter.getNegate() ^ compoundFilter.getNegate());
                return buildFilter(filter, schema);
            })
            .collect(Collectors.toList());

        LogicalOperator operator = getOperatorBasedOnNegate(compoundFilter);

        switch(operator) {
            case AND:
                criteria.andOperator(criterias);
                break;
            case OR:
                criteria.orOperator(criterias);
                break;
        }

        return criteria;
    }

    private static LogicalOperator getOperatorBasedOnNegate(CompoundFilter compoundFilter) {
        if(!compoundFilter.getNegate()) {
            return compoundFilter.getOperator();
        }
        switch(compoundFilter.getOperator()) {
            case AND:
                return LogicalOperator.OR;
            case OR:
                return LogicalOperator.AND;
            default:
                return compoundFilter.getOperator();
        }
    }

}