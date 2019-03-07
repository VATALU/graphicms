package com.graphicms.repository;

import com.graphicms.model.PO.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface UserRepository {
    void findOneByName(String name, Handler<AsyncResult<User>> resultHandler);

    void insert(String userId, String name, String email, String password, Handler<AsyncResult<Void>> resultHandler);

    void findAuthByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findOneByUserId(String userId, Handler<AsyncResult<User>> resultHandler);

    void findUsersByProjectId(String projectId, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    void insertProjectByUserId(String userId, JsonObject project, Handler<AsyncResult<Void>> resultHandler);

    void deleteProjectByUserId(String userId, String projectId, Handler<AsyncResult<Void>> resultHandler);

    void updateProjectAuthByUserId(String userId, String projectId, String auth, Handler<AsyncResult<Void>> resultHandler);

    void findUserByUserName(String userName, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    void insertAuthByUserName(String userName, String projectId, String auth, Handler<AsyncResult<Void>> resultHandler);
}