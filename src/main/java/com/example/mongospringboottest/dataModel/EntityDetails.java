package com.example.mongospringboottest.dataModel;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public class EntityDetails {
    
    private String database;
    private String table;
    private String idColumn;
    private List<String> encryptedColumns = Collections.emptyList();
    private List<ColumnEntityMapping> columnEntityMappings = Collections.emptyList();

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
    public String getDatabase() {
        return database;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public List<String> getEncryptedColumns() {
        return encryptedColumns;
    }
    public void setEncryptedColumns(List<String> encryptedColumns) {
        this.encryptedColumns = encryptedColumns;
    }
    public List<ColumnEntityMapping> getColumnEntityMappings() {
        return columnEntityMappings;
    }
    public void setColumnEntityMappings(List<ColumnEntityMapping> columnEntityMappings) {
        this.columnEntityMappings = columnEntityMappings;
    }
    
}