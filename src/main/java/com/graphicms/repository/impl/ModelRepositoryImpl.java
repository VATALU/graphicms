package com.graphicms.repository.impl;

import com.graphicms.model.PO.Model;
import com.graphicms.repository.ModelRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class ModelRepositoryImpl implements ModelRepository {

    private final MongoClient mongoClient;
    private final static String COLLECTION = "Project";

    public ModelRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void insertModelByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectId);
        JsonObject update = new JsonObject().put("$addToSet", new JsonObject().put("models", model.toJson()));
        mongoClient.updateCollection(COLLECTION, query, update, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject()
                .put("_id", projectId);
        JsonObject update = new JsonObject().put("$pull", new JsonObject()
                .put("models", new JsonObject()
                        .put("_id", modelId)));
        mongoClient.updateCollection(COLLECTION, query, update, res -> {
            if (res.result().getDocModified()!=0) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }
}