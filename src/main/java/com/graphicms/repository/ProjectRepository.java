package com.graphicms.repository;

import com.graphicms.model.PO.Model;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;

public interface ProjectRepository {

    void findAllProjectsByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findModelByModelId(String schemaId, Handler<AsyncResult<Model>> resultHandler);

    void findModelsByProjectId(String projectsId, Handler<AsyncResult<JsonArray>> resultHandler);
}
