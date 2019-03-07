package com.graphicms.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.Map;

public interface CollectionRepository {
    void findCollectionStats(String collection, Handler<AsyncResult<JsonObject>> resultHandler);

    void graphqlQueryDocument(String collection, Map<String, Object> arguments, Handler<AsyncResult<JsonObject>> resultHandler);

    void graphqlUpsertDocument(String collection, Map<String, Object> arguments, Handler<AsyncResult<Void>> resultHandler);

    void deleteDocument(String collection, String itemId, Handler<AsyncResult<Void>> resultHandler);

    void deleteFieldByFieldName(String collection, String fieldName, Handler<AsyncResult<Void>> resultHandler);

    void deleteCollection(String collection, Handler<AsyncResult<Void>> resultHandler);

    void addFieldToDocument(String collection,String fieldName,Handler<AsyncResult<Void>> resultHandler);

    void createDocument(String collection,Handler<AsyncResult<Void>> resultHandler);
}