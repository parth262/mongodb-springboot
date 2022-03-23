package com.example.mongospringboottest.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.example.mongospringboottest.dataModel.EntityDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class EntityDetailsRepository {

    @Autowired
    private ObjectMapper objectMapper;
    
    private Map<String, EntityDetails> entityMapping;

    @PostConstruct
    public void init() throws IOException {
        File file = ResourceUtils.getFile("classpath:entityMapping.json");
        this.entityMapping = objectMapper.readValue(file, new TypeReference<Map<String, EntityDetails>>(){});
    }

    public String getTable(String entity) {
        return this.getEntityDetails(entity).getTable();
    }

    public EntityDetails getEntityDetails(String entity) {
        return this.entityMapping.get(entity);
    }
}
