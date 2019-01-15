package com.graphicms.repository.impl;

import com.graphicms.model.PO.Model;
import com.graphicms.repository.ModelRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class ModelRepositoryImpl implements ModelRepository {

    private final MongoClient mongoClient;

    public ModelRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void insertModelByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> asyncResultHandler) {
        JsonObject query = new JsonObject().put("_id", projectId);
        JsonObject update = new JsonObject().put("$addToSet", new JsonObject().put("models", model.toJson()));
        mongoClient.updateCollection("Project", query, update, res -> {
            if(res.succeeded()) {
                asyncResultHandler.handle(Future.succeededFuture());
            } else {
                asyncResultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }
}