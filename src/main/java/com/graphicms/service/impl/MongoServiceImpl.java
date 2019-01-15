package com.graphicms.service.impl;

import com.graphicms.model.Model;
import com.graphicms.model.User;
import com.graphicms.repository.ModelRepository;
import com.graphicms.repository.ProjectRepository;
import com.graphicms.repository.UserRepository;
import com.graphicms.repository.impl.ModelRepositoryImpl;
import com.graphicms.repository.impl.ProjectRepositoryImpl;
import com.graphicms.repository.impl.UserRepositoryImpl;
import com.graphicms.service.MongoService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        this.modelRepository=new ModelRepositoryImpl(mongoClient);
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
    public void findModelsByProjectId(String projectId, Handler<AsyncResult<JsonArray>> resultHandler) {
        projectRepository.findModelsByProjectId(projectId, resultHandler);
    }

    @Override
    public void insertModelsByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> resultHandler) {
        model.set_id(new ObjectId().toHexString());
        modelRepository.insertModelByProjectId(projectId,model,resultHandler);
    }
}