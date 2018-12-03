package com.graphicms.service;

import com.graphicms.model.User;
import com.graphicms.service.impl.UserServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

@VertxGen
@ProxyGen
public interface UserService {

    static UserService create(MongoClient mongoClient, Handler<AsyncResult<UserService>> resultHandler) {
        return UserServiceImpl.create(mongoClient,resultHandler);
    }

    static UserService createProxy(Vertx vertx, String address) {
        return new UserServiceVertxEBProxy(vertx,address);
    }

    void findOneByName(String name, Handler<AsyncResult<List<User>>> resultHandler);
}