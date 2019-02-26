package com.graphicms.repository.impl;

import com.graphicms.model.PO.Student;
import com.graphicms.repository.CollectionRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class CollectionRepositoryImpl implements CollectionRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionRepositoryImpl.class);
    private final MongoClient mongoClient;

    public CollectionRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void findCollectionStats(String collection, Handler<AsyncResult<JsonObject>> resultHandler) {

    }

    @Override
    public void graphqlCollection(String collection, Map<String, Object> arguments, Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonObject query = new JsonObject();
        Set<Map.Entry<String, Object>> entrySet = arguments.entrySet();
        entrySet.forEach(entry ->
                query.put(entry.getKey(), entry.getValue())
        );
        System.out.println(query);
        mongoClient.findOne(collection, query, null, res -> {
            if (res.result()!=null) {
                System.out.println(res.result());
                resultHandler.handle(Future.succeededFuture(res.result()));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }
}