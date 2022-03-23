package com.example.mongospringboottest.dataModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.USE_DEFAULTS)
public class ColumnEntityMapping {

    private String sourceField;
    private String entity;
    private String foreignField;
    private String newField;

    public String getSourceField() {
        return sourceField;
    }
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }
    public String getEntity() {
        return entity;
    }
    public void setEntity(String entity) {
        this.entity = entity;
    }
    public String getForeignField() {
        return foreignField;
    }
    public void setForeignField(String foreignField) {
        this.foreignField = foreignField;
    }
    public String getNewField() {
        return newField;
    }
    public void setNewField(String newField) {
        this.newField = newField;
    }    
    
}
