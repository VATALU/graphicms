package com.graphicms.repository;

import com.graphicms.model.PO.Field;
import com.graphicms.model.PO.Model;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface ProjectRepository {

    void findAllProjectsByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler);

    void findModelByModelId(String schemaId, Handler<AsyncResult<Model>> resultHandler);

    void findModelsByProjectId(String projectsId, Handler<AsyncResult<JsonArray>> resultHandler);

    void createModelByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> resultHandler);

    void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler);

    void createFieldByProjectIdAndModelId(String projectId, String modelId, Field field, Handler<AsyncResult<Void>> resultHandler);

    void deleteFieldByProjectIdAndModelIdAndName(String projectId, String modelId, JsonObject field, Handler<AsyncResult<Void>> resultHandler);

    void findModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Model>> resultHandler);

    void updateGraphQLTypeField(String projectId, String modelId, String graphQLType, Handler<AsyncResult<Void>> resultHandler);

    void findContentByModelId(String modelId, Handler<AsyncResult<JsonArray>> resultHandler);
}
