package com.graphicms.repository.impl;

import com.graphicms.model.PO.User;
import com.graphicms.repository.UserRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepositoryImpl implements UserRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final static String COLLECTION = "User";
    private final MongoClient mongoClient;

    public UserRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void findOneByName(String name, Handler<AsyncResult<User>> resultHandler) {
        JsonObject query = new JsonObject().put("name", name);
        mongoClient.findOne(COLLECTION, query, null, res -> {
            if (res.result() != null) {
                User user = new User(res.result());
                resultHandler.handle(Future.succeededFuture(user));
            } else {
                LOGGER.error("DataBase query error: {}", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void findOneByUserId(String userId, Handler<AsyncResult<User>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", userId);
        mongoClient.findOne(COLLECTION, query, null, res -> {
            if (res.result() != null) {
                User user = new User(res.result());
                resultHandler.handle(Future.succeededFuture(user));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void insert(String name, String email, String password, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject user = new JsonObject().put("name", name).put("email", email).put("password", password);
        mongoClient.insert(COLLECTION, user, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void findAuthByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", userId);
        JsonObject fields = new JsonObject().put("projects", 1);
        mongoClient.findOne(COLLECTION, query, fields, res -> {
            if (res.result() != null) {
                JsonArray auths = res.result().getJsonArray("projects");
                resultHandler.handle(Future.succeededFuture(auths));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }
}