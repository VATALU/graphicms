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

import java.util.List;
import java.util.stream.Collectors;

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

    public void findOneByName(String name, Handler<AsyncResult<List<User>>> resultHandler) {
        JsonObject query = new JsonObject().put("name", name);
        mongoClient.find("User", query, res -> {
            if (res.succeeded()) {
                List<User> users = res.result().stream()
                        .map(r -> r.mapTo(User.class))
                        .collect(Collectors.toList());
                resultHandler.handle(Future.succeededFuture(users));
            } else {
                LOGGER.error("DataBase query error: {}", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

}