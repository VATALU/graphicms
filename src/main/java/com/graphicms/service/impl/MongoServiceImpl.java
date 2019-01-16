package com.graphicms.service.impl;

import com.graphicms.model.PO.Model;
import com.graphicms.model.PO.User;
import com.graphicms.repository.ModelRepository;
import com.graphicms.repository.ProjectRepository;
import com.graphicms.repository.UserRepository;
import com.graphicms.repository.impl.ModelRepositoryImpl;
import com.graphicms.repository.impl.ProjectRepositoryImpl;
import com.graphicms.repository.impl.UserRepositoryImpl;
import com.graphicms.service.MongoService;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoServiceImpl implements MongoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoServiceImpl.class);


    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private ModelRepository modelRepository;

    public MongoServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, ModelRepository modelRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.modelRepository = modelRepository;
    }

    public static MongoServiceImpl create(MongoClient mongoClient, Handler<AsyncResult<MongoService>> resultHandler) {
        MongoServiceImpl userService = new MongoServiceImpl(mongoClient);
        resultHandler.handle(Future.succeededFuture(userService));
        return userService;
    }

    private MongoServiceImpl(MongoClient mongoClient) {
        this.userRepository = new UserRepositoryImpl(mongoClient);
        this.projectRepository = new ProjectRepositoryImpl(mongoClient);
        this.modelRepository = new ModelRepositoryImpl(mongoClient);
    }

    @Override
    public void findUserByName(String name, Handler<AsyncResult<User>> resultHandler) {
        userRepository.findOneByName(name, resultHandler);
    }

    @Override
    public void createUser(String name, String email, String password, Handler<AsyncResult<Void>> resultHandler) {
        userRepository.findOneByName(name, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.failedFuture("Username Duplicate"));
            } else {
                userRepository.insert(name, email, password, resultHandler);
            }
        });
    }

    @Override
    public void findAllProjectsByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler) {
        projectRepository.findAllProjectsByUserId(userId, resultHandler);
    }

    @Override
    public void findAllProjectInfosByUserId(String userId, Handler<AsyncResult<JsonArray>> resultHandler) {
        Future<JsonArray> projectsFuture = Future.future();
        projectRepository.findAllProjectsByUserId(userId, projectsFuture.completer());
        Future<JsonArray> authsFuture = Future.future();
        userRepository.findAuthByUserId(userId, authsFuture.completer());
        CompositeFuture.all(projectsFuture, authsFuture).setHandler(res -> {
            if (res.succeeded()) {
                JsonArray projects = projectsFuture.result();
                JsonArray auths = authsFuture.result();
                JsonArray projectInfos=new JsonArray();
                for (int i = 0; i < projects.size(); i++) {
                    JsonObject project = projects.getJsonObject(i);
                    String projectId = project.getString("_id");
                    for (int j = 0; j < auths.size(); j++) {
                        if (auths.getJsonObject(j).getString("_id").equals(projectId)) {
                            project.put("auth", auths.getJsonObject(j).getString("auth"));
                            auths.remove(j);
                            break;
                        }
                    }
                    projectInfos.add(project);
                }
                resultHandler.handle(Future.succeededFuture(projectInfos));
            }
        });
    }

    @Override
    public void findModelsByProjectId(String projectId, Handler<AsyncResult<JsonArray>> resultHandler) {
        projectRepository.findModelsByProjectId(projectId, resultHandler);
    }

    @Override
    public void insertModelsByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> resultHandler) {
        model.set_id(new ObjectId().toHexString());
        modelRepository.insertModelByProjectId(projectId, model, resultHandler);
    }

    @Override
    public void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler) {
        modelRepository.deleteModelByProjectIdAndModelId(projectId, modelId, resultHandler);
        //TODO delete model document
    }
}