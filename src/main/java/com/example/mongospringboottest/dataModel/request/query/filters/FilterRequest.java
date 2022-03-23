package com.example.mongospringboottest.dataModel.request.query.filters;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @Type(value = ComparisonFilter.class, name = "comparison"),
    @Type(value = BetweenFilter.class, name = "between"),
    @Type(value = INFilter.class, name="in"),
    @Type(value = CompoundFilter.class, name="compound")
})
@Schema(
    name = "Filter", 
    subTypes = {
        CompoundFilter.class, 
        BetweenFilter.class, 
        INFilter.class, 
        CompoundFilter.class
    },
    discriminatorProperty = "type"
)
public class FilterRequest {
    protected FilterType type;
    private Boolean negate;

    public FilterRequest() {
    }

    public FilterRequest(FilterType type) {
        this.type = type;
        this.negate = false;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public Boolean getNegate() {
        return negate;
    }

    public void setNegate(Boolean negate) {
        this.negate = negate;
    }

    @JsonIgnore
    public Map<String, List<Object>> getFieldValueMap() {
        switch(this.type) {
            case COMPOUND:
                CompoundFilter compoundFilter = (CompoundFilter) this;
                return compoundFilter.getAllFieldValueMap();
            default:
                SimpleFilter simpleFilter = (SimpleFilter) this;
                return Map.of(simpleFilter.getField(), simpleFilter.getValues());
        }
    }
}
