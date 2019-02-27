package com.graphicms.repository.impl;

import com.graphicms.repository.CollectionRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
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
    public void graphqlQueryDocument(String collection, Map<String, Object> arguments, Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonObject query = new JsonObject();
        Set<Map.Entry<String, Object>> entrySet = arguments.entrySet();
        entrySet.forEach(entry ->
                query.put(entry.getKey(), entry.getValue())
        );
        System.out.println(query);
        mongoClient.findOne(collection, query, null, res -> {
            if (res.result() != null) {
                System.out.println(res.result());
                resultHandler.handle(Future.succeededFuture(res.result()));
            } else {
                System.out.println(res.result());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void graphqlUpsertDocument(String collection, Map<String, Object> arguments, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject update = new JsonObject();
        Set<Map.Entry<String, Object>> entrySet = arguments.entrySet();
        entrySet.forEach(entry ->
                update.put(entry.getKey(), entry.getValue())
        );
        mongoClient.save(collection, update, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteDocument(String collection, String itemId, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", itemId);
        mongoClient.findOneAndDelete(collection, query, res -> {
            if (res.result() != null) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteFieldByFieldName(String collection, String fieldName, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject update = new JsonObject().put("$unset", new JsonObject().put(fieldName, ""));

        mongoClient.updateCollectionWithOptions(collection, new JsonObject(), update, new UpdateOptions().setUpsert(false).setMulti(true), res -> {
            if (res.result() != null) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteCollection(String collection, Handler<AsyncResult<Void>> resultHandler) {
        mongoClient.dropCollection(collection, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void addFieldToDocument(String collection, String fieldName, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject update = new JsonObject().put("$set", new JsonObject().put(fieldName, ""));
        mongoClient.updateCollectionWithOptions(collection, new JsonObject(), update, new UpdateOptions().setMulti(true), res -> {
            if (res.result() != null) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void createDocument(String collection, Handler<AsyncResult<Void>> resultHandler) {
        mongoClient.createCollection(collection, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });

    }


}