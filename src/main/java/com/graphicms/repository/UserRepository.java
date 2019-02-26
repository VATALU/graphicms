package com.graphicms.repository;

import com.graphicms.model.PO.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;

public interface UserRepository {
    void findOneByName(String name, Handler<AsyncResult<User>> resultHandler);

    void insert(String name, String email, String password, Handler<AsyncResult<Void>> resultHandler);

    void findAuthByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findOneByUserId(String userId, Handler<AsyncResult<User>> resultHandler);
}