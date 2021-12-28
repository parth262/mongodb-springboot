package com.example.mongospringboottest.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.mongospringboottest.domain.query.Paging;
import com.example.mongospringboottest.domain.query.Query;
import com.example.mongospringboottest.domain.query.Sort;
import com.mongodb.client.FindIterable;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DataRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    private Document removeId(Document document) {
        document.remove("_id");
        return document;
    }

    private Bson prepareSortingFilter(List<Sort> sorting) {
        BsonDocument bson = new BsonDocument();
        sorting.forEach(sort -> {
            BsonValue value = new BsonInt32(sort.isAscending ? 1 : -1);
            bson.put(sort.field, value);
        });
        return bson;
    }

    public FindIterable<Document> commonQuery(String table, Integer skip, Integer limit) {
        return mongoTemplate.getCollection(table).find()
            .skip(skip)
            .limit(limit);
    }

    public List<Document> query(String table, Integer skip, Integer limit) {
        return commonQuery(table, skip, limit)
            .map(this::removeId)
            .into(new ArrayList<>());
    }

    public List<Document> query(String table, Query query) {
        Paging paging = query.paging;
        Integer skip = paging.pageNumber*paging.pageSize;
        Integer limit = paging.pageSize;
        FindIterable<Document> documents = commonQuery(table, skip, limit);

        if(!query.sorting.isEmpty()) {
            Bson sortingFilter = this.prepareSortingFilter(query.sorting);
            documents = documents.sort(sortingFilter);
        }

        return documents
            .map(this::removeId)
            .into(new ArrayList<>());
    }

    public Long getTotalCount(String table) {
        return mongoTemplate.getCollection(table).countDocuments();
    }
}
