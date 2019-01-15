package com.graphicms.repository.impl;

import com.graphicms.model.Model;
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

    private final MongoClient mongoClient;

    public ProjectRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void findAllProjectsByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler) {
        JsonArray pipeline = new JsonArray()
                .add(new JsonObject().put("$match", new JsonObject().put("_id", userId)))
                .add(new JsonObject().put("$lookup", new JsonObject().put("localField", "projects._id")
                        .put("from", "Project")
                        .put("foreignField", "_id")
                        .put("as", "project")))
                .add(new JsonObject().put("$project", new JsonObject()
                        .put("projects", 1)
                        .put("project", 1)
                        .put("_id", 0)));
        mongoClient.aggregate("User", pipeline)
                .exceptionHandler(res -> resultHandler.handle(Future.failedFuture(res.getCause())))
                .handler(result -> {
                    JsonArray auth = result.getJsonArray("projects");
                    JsonArray projects = result.getJsonArray("project");
                    JsonArray jsonProjects = new JsonArray();
                    //fetch auth to project
                    for (int i = 0; i < projects.size(); i++) {
                        JsonObject project = projects.getJsonObject(i);
                        String projectId = project.getString("_id");
                        for (int j = 0; j < auth.size(); j++) {
                            if (auth.getJsonObject(j).getString("_id").equals(projectId)) {
                                project.put("auth", auth.getJsonObject(j).getString("auth"));
                                auth.remove(j);
                                break;
                            }
                        }
                        jsonProjects.add(project);
                    }
                    resultHandler.handle(Future.succeededFuture(jsonProjects));
                });
    }

    @Override
    public void findModelByModelId(String modelId, Handler<AsyncResult<Model>> resultHandler) {
        JsonObject query = new JsonObject().put("models.name", new JsonObject().put("$all", new JsonArray().add(modelId)));
        JsonObject fields = new JsonObject().put("models", 1).put("_id", 0);
        mongoClient.findOne("Project", query, fields, res -> {
            if (res.result() != null) {
                resultHandler.handle(Future.succeededFuture(new Model(res.result())));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void findModelsByProjectId(String projectsId, Handler<AsyncResult<JsonArray>> resultHandler) {
        JsonObject query = new JsonObject().put("_id", projectsId);
        JsonObject field = new JsonObject().put("_id", 0).put("models", "1");
        mongoClient.findOne("Project", query, field, res -> {
            if (res.result() != null) {
                JsonArray models = res.result().getJsonArray("models");
                resultHandler.handle(Future.succeededFuture(models));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }


}