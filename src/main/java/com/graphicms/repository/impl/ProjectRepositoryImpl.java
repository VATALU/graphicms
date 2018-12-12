package com.graphicms.repository.impl;

import com.graphicms.model.Project;
import com.graphicms.repository.ProjectRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProjectRepositoryImpl.class);

    private final MongoClient mongoClient;

    public ProjectRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void findAllProjectsByUserId(String userId, Handler<AsyncResult<List<Project>>> resultHandler) {
        JsonArray pipeline = new JsonArray()
                .add(new JsonObject().put("$match", new JsonObject().put("_id", userId)))
                .add(new JsonObject().put("$lookup", new JsonObject().put("localField", "groups")
                        .put("from", "Project")
                        .put("foreignField", "group")
                        .put("as", "projects")));
        mongoClient.aggregate("User", pipeline).handler(result -> {
            JsonArray projects = result.getJsonArray("projects");
            List<Project> projectList = new ArrayList<>();
            for (int i = 0; i < projects.size(); i++) {
                projects.getJsonObject(i);
                projects.getJsonObject(i).mapTo(Project.class);
                projectList.add(projects.getJsonObject(i).mapTo(Project.class));
            }
            resultHandler.handle(Future.succeededFuture(projectList));
        });
    }

}