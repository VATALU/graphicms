package com.graphicms.repository;

import com.graphicms.model.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.mongo.MongoClient;

public interface UserRepository {
    void findOneByName(String name, Handler<AsyncResult<User>> resultHandler);

    void insert(String name, String email, String password, Handler<AsyncResult<Void>> resultHandler);

}