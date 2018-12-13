package com.graphicms.service.impl;

import com.graphicms.model.Project;
import com.graphicms.model.User;
import com.graphicms.repository.ProjectRepository;
import com.graphicms.repository.UserRepository;
import com.graphicms.repository.impl.ProjectRepositoryImpl;
import com.graphicms.repository.impl.UserRepositoryImpl;
import com.graphicms.service.MongoService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MongoServiceImpl implements MongoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoServiceImpl.class);


    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    public static MongoServiceImpl create(MongoClient mongoClient, Handler<AsyncResult<MongoService>> resultHandler) {
        MongoServiceImpl userService = new MongoServiceImpl(mongoClient);
        resultHandler.handle(Future.succeededFuture(userService));
        return userService;
    }

    private MongoServiceImpl(MongoClient mongoClient) {
        this.userRepository = new UserRepositoryImpl(mongoClient);
        this.projectRepository = new ProjectRepositoryImpl(mongoClient);
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
    public void findAllProjectsByUserId(String userId, Handler<AsyncResult<List<Project>>> resultHandler) {
        projectRepository.findAllProjectsByUserId(userId, resultHandler);
    }
}