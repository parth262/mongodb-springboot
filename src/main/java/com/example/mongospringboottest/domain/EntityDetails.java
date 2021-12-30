package com.example.mongospringboottest.domain;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public class EntityDetails {
    private String table;
    private String idColumn;
    private String idType;
    private Map<String, String> columnEntityMap = Collections.emptyMap();

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }
    public String getIdColumn() {
        return idColumn;
    }
    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }
    public Map<String, String> getColumnEntityMap() {
        return columnEntityMap;
    }
    public void setColumnEntityMap(Map<String, String> columnEntityMap) {
        this.columnEntityMap = columnEntityMap;
    }
}