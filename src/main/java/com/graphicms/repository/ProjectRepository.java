package com.graphicms.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ProjectRepository {

    void findAllProjectsByUserId(String userId, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    void findSchemaBySchemaId(String schemaId, Handler<AsyncResult<JsonObject>> resultHandler);

    void findModelsByProjectId(String projectsId, Handler<AsyncResult<List<JsonObject>>> resultHandler);
}
