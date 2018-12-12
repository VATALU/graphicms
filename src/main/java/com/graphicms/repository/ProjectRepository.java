package com.graphicms.repository;

import com.graphicms.model.Project;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

public interface ProjectRepository {

    void findAllProjectsByUserId(String userId, Handler<AsyncResult<List<Project>>> resultHandler);
}
