package com.graphicms.service.impl;

import com.graphicms.model.PO.*;
import com.graphicms.repository.CollectionRepository;
import com.graphicms.repository.ProjectRepository;
import com.graphicms.repository.UserRepository;
import com.graphicms.repository.impl.CollectionRepositoryImpl;
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

import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

import static graphql.schema.GraphQLArgument.newArgument;

public class MongoServiceImpl implements MongoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoServiceImpl.class);


    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private CollectionRepository collectionRepository;

    public MongoServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, CollectionRepository collectionRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.collectionRepository = collectionRepository;
    }

    public static MongoServiceImpl create(MongoClient mongoClient, Handler<AsyncResult<MongoService>> resultHandler) {
        MongoServiceImpl userService = new MongoServiceImpl(mongoClient);
        resultHandler.handle(Future.succeededFuture(userService));
        return userService;
    }

    private MongoServiceImpl(MongoClient mongoClient) {
        this.userRepository = new UserRepositoryImpl(mongoClient);
        this.projectRepository = new ProjectRepositoryImpl(mongoClient);
        this.collectionRepository = new CollectionRepositoryImpl(mongoClient);
    }

    @Override
    public void findUserByName(String name, Handler<AsyncResult<User>> resultHandler) {
        userRepository.findOneByName(name, resultHandler);
    }

    @Override
    public void findUserByUserId(String userId, Handler<AsyncResult<User>> resultHandler) {
        userRepository.findOneByUserId(userId, resultHandler);
    }

    @Override
    public void createUser(String name, String email, String password, Handler<AsyncResult<String>> resultHandler) {
        userRepository.findOneByName(name, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.failedFuture("Username Duplicate"));
            } else {
                String userId = new ObjectId().toHexString();
                userRepository.insert(userId, name, email, password, res2 -> {
                    if (res2.succeeded()) {
                        resultHandler.handle(Future.succeededFuture(userId));
                    } else {
                        resultHandler.handle(Future.failedFuture(res.cause()));

                    }
                });
            }
        });
    }

    @Override
    public void findOwnersByProjectId(String projectId, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        userRepository.findUsersByProjectId(projectId, resultHandler);
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
                JsonArray projectInfos = new JsonArray();
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
    public void findModelByProjectIdAndModelId(String porjectId, String modelId, Handler<AsyncResult<Model>> resultHandler) {
        projectRepository.findModelByProjectIdAndModelId(porjectId, modelId, resultHandler);
    }

    @Override
    public void insertModelsByProjectId(String projectId, Model model, Handler<AsyncResult<String>> resultHandler) {
        String modelId = new ObjectId().toHexString();
        model.set_id(modelId);
        projectRepository.createModelByProjectId(projectId, model, res -> {
            if (res.succeeded()) {
                //创建model document
                collectionRepository.createDocument(modelId, res2 -> {
                    if (res2.succeeded()) {
                        resultHandler.handle(Future.succeededFuture(modelId));
                    } else {
                        resultHandler.handle(Future.failedFuture(res.cause()));
                    }
                });
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler) {
        projectRepository.deleteModelByProjectIdAndModelId(projectId, modelId, res -> {
            if (res.succeeded()) {
                //delete model document
                collectionRepository.deleteCollection(modelId, resultHandler);
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void createFieldByProjectIdAndModelId(String projectId, String modelId, JsonObject field, Handler<AsyncResult<Void>> resultHandler) {
        projectRepository.createFieldByProjectIdAndModelId(projectId, modelId, new Field(field), res1 -> {
            if (res1.succeeded()) {
                //add document field
                collectionRepository.addFieldToDocument(modelId, field.getString("name"), resultHandler);
            } else {
                resultHandler.handle(Future.failedFuture(res1.cause()));
            }
        });

    }

    @Override
    public void deleteField(String projectId, String modelId, String fieldName, Handler<AsyncResult<Void>> resultHandler) {
        JsonObject field = new JsonObject().put("name", fieldName);
        projectRepository.deleteFieldByProjectIdAndModelIdAndName(projectId, modelId, field, res1 -> {
            if (res1.succeeded()) {
                // delete document field
                collectionRepository.deleteFieldByFieldName(modelId, fieldName, resultHandler);
            } else {
                resultHandler.handle(Future.failedFuture(res1.cause()));
            }
        });
    }

    @Override
    public void findContentByModels(String modelId, Handler<AsyncResult<JsonArray>> resultHandler) {
        projectRepository.findContentByModelId(modelId, resultHandler);
    }

    @Override
    public void graphql(String modelId, String graphqlQuery, Handler<AsyncResult<JsonObject>> resultHandler) {

    }

    @Override
    public void findModelByModelId(String modelId, Handler<AsyncResult<Model>> resultHandler) {
        projectRepository.findModelByModelId(modelId, resultHandler);
    }

    @Override
    public void qraphqlQuery(String collection, JsonObject arguments, Handler<AsyncResult<JsonObject>> resultHandler) {
        Map<String, Object> argumentsMap = arguments.getMap();
        collectionRepository.graphqlQueryDocument(collection, argumentsMap, resultHandler);
    }

    @Override
    public void graphqlMutation(String collection, JsonObject arguments, Handler<AsyncResult<Void>> resultHandler) {
        Map<String, Object> argumentsMap = arguments.getMap();
        collectionRepository.graphqlUpsertDocument(collection, argumentsMap, resultHandler);
    }

    @Override
    public void deleteDocumentByItemId(String collection, String modelId, Handler<AsyncResult<Void>> resultHandler) {
        collectionRepository.deleteDocument(collection, modelId, resultHandler);
    }

    @Override
    public void createProjectByUserId(String userId, String auth, Project project, Handler<AsyncResult<String>> resultHandler) {
        String projectId = new ObjectId().toHexString();
        project.set_id(projectId);
        Auth authority = new Auth();
        authority.set_id(projectId);
        authority.setAuth(auth);
        userRepository.insertProjectByUserId(userId, authority.toJson(), res -> {
            if (res.succeeded()) {
                projectRepository.createProject(project, res2 -> {
                    if (res.succeeded()) {
                        resultHandler.handle(Future.succeededFuture(projectId));
                    } else {
                        resultHandler.handle(Future.failedFuture(res.cause()));
                    }
                });
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteProjectByUserIdAndProjectId(String userId, String projectId, Handler<AsyncResult<Void>> resultHandler) {
        Future<Void> deleteProjectFuture = Future.future();
        projectRepository.deleteProject(projectId, deleteProjectFuture.completer());
        deleteProjectFuture.compose(res -> {
            Future<Void> future = Future.future();
            userRepository.deleteProjectByUserId(userId, projectId, future);
            return future;
        }).setHandler(res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void updateProjectAuthByUserIdAndProjectId(String userId, String projectId, String auth, Handler<AsyncResult<Void>> resultHandler) {
        userRepository.updateProjectAuthByUserId(userId, projectId, auth, resultHandler);
    }

    @Override
    public void findUserByUserName(String userName, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        userRepository.findUserByUserName(userName, resultHandler);
    }

    @Override
    public void insertAuthByUserName(String userName, String projectId, String auth, Handler<AsyncResult<Void>> resultHandler) {
        userRepository.insertAuthByUserName(userName, projectId, auth, resultHandler);
    }

    @Override
    public void findModelSizeByModelId(String modelId, Handler<AsyncResult<Integer>> resultHandler) {
        collectionRepository.findCollectionStats(modelId,res->{
            if(res.succeeded()) {
                 resultHandler.handle(Future.succeededFuture(res.result().getInteger("size")));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }


}