package com.graphicms.repository.impl;

import com.graphicms.model.PO.Field;
import com.graphicms.model.PO.Model;
import com.graphicms.repository.ProjectRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProjectRepositoryImpl.class);
    private final static String COLLECTION = "Project";
    private final MongoClient mongoClient;

    public ProjectRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void findAllProjectsByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler) {
        JsonArray pipeline = new JsonArray()
                .add(new JsonObject().put("$match", new JsonObject().put("_id", userId)))
                .add(new JsonObject().put("$lookup", new JsonObject().put("localField", "projects._id")
                        .put("from", COLLECTION)
                        .put("foreignField", "_id")
                        .put("as", "projects")))
                .add(new JsonObject().put("$project", new JsonObject()
                        .put("projects", 1)
                        .put("_id", 0)));
        mongoClient.aggregate("User", pipeline)
                .exceptionHandler(res -> resultHandler.handle(Future.failedFuture(res.getCause())))
                .handler(result -> {
                    JsonArray projects = result.getJsonArray("projects");
                    resultHandler.handle(Future.succeededFuture(projects));
                });
    }

    @Override
    public void findModelByModelId(String modelId, Handler<AsyncResult<Model>> resultHandler) {
        JsonObject query = new JsonObject().put("models._id", modelId);
        JsonObject field = new JsonObject().put("models", new JsonObject().put("$elemMatch", new JsonObject().put("_id", modelId)));
        mongoClient.findOne(COLLECTION, query, field, res -> {
            if (res.succeeded()) {
                Model model = new Model(res.result().getJsonArray("models").getJsonObject(0));
                resultHandler.handle(Future.succeededFuture(model));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void findModelsByProjectId(String projectsId, Handler<AsyncResult<JsonArray>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectsId);
        JsonObject field = new JsonObject().put("_id", 0).put("models", "1");
        mongoClient.findOne(COLLECTION, query, field, res -> {
            if (res.result() != null) {
                JsonArray models = res.result().getJsonArray("models");
                resultHandler.handle(Future.succeededFuture(models));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }


    @Override
    public void createModelByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectId);
        JsonObject update = new JsonObject().put("$addToSet", new JsonObject().put("models", model.toJson()));
        mongoClient.updateCollection(COLLECTION, query, update, res -> {
            if (res.result().getDocModified() > 0) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void findModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Model>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectId).put("models._id", modelId);
        JsonObject field = new JsonObject().put("models", new JsonObject().put("$elemMatch", new JsonObject().put("_id", modelId)));
        mongoClient.findOne(COLLECTION, query, field, res -> {
            if (res.succeeded()) {
                Model model = new Model(res.result().getJsonArray("models").getJsonObject(0));
                resultHandler.handle(Future.succeededFuture(model));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void updateGraphQLTypeField(String projectId, String modelId, String graphQLType, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectId).put("models._id", modelId);
        JsonObject update = new JsonObject().put("$set", new JsonObject().put("models.$.graphqlType", graphQLType));
        mongoClient.updateCollection(COLLECTION, query, update, res -> {
            if (res.result().getDocModified() > 0) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void findContentByModelId(String modelId, Handler<AsyncResult<JsonArray>> resultHandler) {
        mongoClient.find(modelId, new JsonObject(), res -> {
            if (res.result().size() > 0) {
                JsonArray jsonArray = new JsonArray();
                for(JsonObject jsonObject :res.result()) {
                    jsonArray.add(jsonObject);
                }
                resultHandler.handle(Future.succeededFuture(jsonArray));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void createFieldByProjectIdAndModelId(String projectId, String modelId, Field field, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectId).put("models._id", modelId);
        JsonObject update = new JsonObject().put("$addToSet", new JsonObject().put("models.$.fields", field.toJson()));
        mongoClient.updateCollection(COLLECTION, query, update, res -> {
            if (res.result().getDocModified() > 0) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteFieldByProjectIdAndModelIdAndName(String projectId, String modelId, JsonObject field, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectId).put("models._id", modelId);
        JsonObject update = new JsonObject().put("$pull", new JsonObject().put("models.$.fields", field));
        mongoClient.updateCollection(COLLECTION, query, update, res -> {
            if (res.result().getDocModified() > 0) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject query = new JsonObject()
                .put("_id", projectId);
        JsonObject update = new JsonObject().put("$pull", new JsonObject()
                .put("models", new JsonObject()
                        .put("_id", modelId)));
        mongoClient.updateCollection(COLLECTION, query, update, res -> {
            if (res.result().getDocModified() > 0) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

}