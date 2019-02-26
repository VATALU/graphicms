package com.graphicms.service;

import com.graphicms.model.PO.Field;
import com.graphicms.model.PO.Model;
import com.graphicms.model.PO.User;
import com.graphicms.service.impl.MongoServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Map;

@VertxGen
@ProxyGen
public interface MongoService {

    static MongoService create(MongoClient mongoClient, Handler<AsyncResult<MongoService>> resultHandler) {
        return MongoServiceImpl.create(mongoClient, resultHandler);
    }

    static MongoService createProxy(Vertx vertx, String address) {
        return new MongoServiceVertxEBProxy(vertx, address);
    }

    void findUserByName(String name, Handler<AsyncResult<User>> resultHandler);

    void findUserByUserId(String userId, Handler<AsyncResult<User>> resultHandler);

    void createUser(String name, String email, String password, Handler<AsyncResult<Void>> resultHandler);

    void findAllProjectsByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findAllProjectInfosByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findModelsByProjectId(String projectId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Model>> resultHandler);

    void insertModelsByProjectId(String projectId, Model model, Handler<AsyncResult<String>> resultHandler);

    void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler);

    void createFieldByProjectIdAndModelId(String projectId, String modelId, JsonObject field, Handler<AsyncResult<Void>> resultHandler);

    void deleteField(String projectId, String modelId, String fieldName, Handler<AsyncResult<Void>> resultHandler);

    void findContentByModels(String modelId, Handler<AsyncResult<JsonArray>> resultHandler);

    void graphql(String modelId, String graphqlQuery, Handler<AsyncResult<JsonObject>> resultHandler);

    void findModelByModelId(String modelId, Handler<AsyncResult<Model>> resultHandler);

    void qraphqlQuery(String collection, JsonObject arguments, Handler<AsyncResult<JsonObject>> resultHandler);
}