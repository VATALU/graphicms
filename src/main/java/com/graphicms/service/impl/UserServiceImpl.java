package com.graphicms.service.impl;

import com.graphicms.model.User;
import com.graphicms.service.UserService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private MongoClient mongoClient;

    public static UserServiceImpl create(MongoClient mongoClient, Handler<AsyncResult<UserService>> resultHandler) {
        UserServiceImpl userService = new UserServiceImpl(mongoClient);
        resultHandler.handle(Future.succeededFuture(userService));
        return userService;
    }

    private UserServiceImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void findOneByName(String name, Handler<AsyncResult<User>> resultHandler) {
        JsonObject query = new JsonObject().put("name", name);
        mongoClient.findOne("User", query, null, res -> {
            if (res.result() != null) {
                resultHandler.handle(Future.succeededFuture(res.result().mapTo(User.class)));
            } else {
                LOGGER.error("DataBase query error: {}", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void insert(String name, String email, String password, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject user = new JsonObject().put("name", name).put("email", email).put("password", password);
        mongoClient.insert("User", user, res -> {
            if(res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }
}