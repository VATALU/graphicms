package com.graphicms.repository;

import com.graphicms.model.Model;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ProjectRepository {

    void findAllProjectsByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findModelByModelId(String schemaId, Handler<AsyncResult<Model>> resultHandler);

    void findModelsByProjectId(String projectsId, Handler<AsyncResult<JsonArray>> resultHandler);
}
