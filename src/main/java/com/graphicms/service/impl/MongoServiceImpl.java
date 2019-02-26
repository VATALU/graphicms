package com.graphicms.service.impl;

import com.graphicms.graphQL.dataFetcher.AsyncDataFetcher;
import com.graphicms.graphQL.queryExecutor.AsyncGraphQLExec;
import com.graphicms.model.PO.Field;
import com.graphicms.model.PO.Model;
import com.graphicms.model.PO.User;
import com.graphicms.repository.CollectionRepository;
import com.graphicms.repository.ProjectRepository;
import com.graphicms.repository.UserRepository;
import com.graphicms.repository.impl.CollectionRepositoryImpl;
import com.graphicms.repository.impl.ProjectRepositoryImpl;
import com.graphicms.repository.impl.UserRepositoryImpl;
import com.graphicms.service.MongoService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.*;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
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

import javax.swing.plaf.FontUIResource;
import java.util.HashMap;
import java.util.Map;

import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

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
        model.setGraphqlType("type " + model.getName() + "{}");
        model.set_id(modelId);
        projectRepository.createModelByProjectId(projectId, model, res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture(modelId));
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler) {
        projectRepository.deleteModelByProjectIdAndModelId(projectId, modelId, resultHandler);
        //TODO delete model document
    }

    @Override
    public void createFieldByProjectIdAndModelId(String projectId, String modelId, JsonObject field, Handler<AsyncResult<Void>> resultHandler) {
        projectRepository.createFieldByProjectIdAndModelId(projectId, modelId, new Field(field), res1 -> {
            if (res1.succeeded()) {
                projectRepository.findModelByProjectIdAndModelId(projectId, modelId, res2 -> {
                    if (res2.succeeded()) {
                        Model model = res2.result();
                        String graphqlType = model.getGraphqlType();
                        String newField = " " + field.getString("name") + ":" + field.getString("type");
                        StringBuilder stringBuilder = new StringBuilder(graphqlType);
                        stringBuilder.insert(graphqlType.indexOf("}"), newField);
                        String newGraphqlType = stringBuilder.toString();
                        projectRepository.updateGraphQLTypeField(projectId, modelId, newGraphqlType, resultHandler);
                    } else {
                        //TODO roll back
                        resultHandler.handle(Future.failedFuture(res2.cause()));
                    }
                });
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
                projectRepository.findModelByProjectIdAndModelId(projectId, modelId, res2 -> {
                    if (res2.succeeded()) {
                        String newGrahqlType;
                        String graphqlType = res2.result().getGraphqlType();
                        int index = graphqlType.indexOf(fieldName);
                        String s = graphqlType.substring(index);
                        int spaceIndex = s.indexOf(" ");
                        if (spaceIndex == -1) {
                            newGrahqlType = graphqlType.substring(0, index) + "}";
                        } else {
                            newGrahqlType = graphqlType.substring(0, index) + s.substring(spaceIndex);
                        }
                        projectRepository.updateGraphQLTypeField(projectId, modelId, newGrahqlType, resultHandler);
                    } else {
                        //TODO roll back
                        resultHandler.handle(Future.failedFuture(res1.cause()));
                    }
                });
            } else {
                resultHandler.handle(Future.failedFuture(res1.cause()));
            }
        });
        //TODO delete document field
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
        Map<String,Object> argumentsMap=arguments.getMap();
        collectionRepository.graphqlCollection(collection, argumentsMap, resultHandler);
    }


}