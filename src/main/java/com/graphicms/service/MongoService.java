package com.graphicms.service;

import com.graphicms.model.Model;
import com.graphicms.model.User;
import com.graphicms.service.impl.MongoServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

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

    void createUser(String name, String email, String password, Handler<AsyncResult<Void>> resultHandler);

    void findAllProjectsByUserId(String userId, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    void findModelsByProjectId(String projectId, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    void insertModelsByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> resultHandler);
}