package com.graphicms.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.Map;

public interface CollectionRepository {
    void findCollectionStats(String collection, Handler<AsyncResult<JsonObject>> resultHandler);

    void graphqlCollection(String collection,Map<String, Object> arguments,Handler<AsyncResult<JsonObject>> resultHandler);
}