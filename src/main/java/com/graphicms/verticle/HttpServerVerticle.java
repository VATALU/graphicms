package com.graphicms.verticle;

import com.graphicms.controller.ProjectController;
import com.graphicms.controller.UserController;
import com.graphicms.service.MongoService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static graphql.schema.GraphQLArgument.newArgument;

/**
 * server verticle
 */
public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private static final String SERVER_PORT = "server.port";
    private static final String MONGO_ADDRESS = "address.mongo";

    @Override
    public void start(Future<Void> startFuture) {

        MongoService mongoService = MongoService.createProxy(vertx, config().getString(MONGO_ADDRESS));
        JWTAuth jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setPublicKey("keyboard cat")
                        .setSymmetric(true)));
        UserController userController = new UserController(mongoService, jwtAuth);
        ProjectController projectController = new ProjectController(mongoService);

        //create server
        HttpServer server = vertx.createHttpServer();

        //set graphql
//        AsyncDataFetcher<JsonObject> userFetcher = (env, handler) -> {
//            String name = env.getArgument("name");
//            mongoService.findUserByName(name, handler);
//        };
//        GraphQLObjectType user = GraphQLObjectType.newObject()
//                .name("user")
//                .field(GraphQLFieldDefinition.newFieldDefinition()
//                        .name("_id")
//                        .type(Scalars.GraphQLString)
//                )
//                .field(GraphQLFieldDefinition.newFieldDefinition()
//                        .name("name")
//                        .type(Scalars.GraphQLString)
//                )
//                .field(GraphQLFieldDefinition.newFieldDefinition()
//                        .name("password")
//                        .type(Scalars.GraphQLString))
//                .build();
//
//        GraphQLObjectType query = GraphQLObjectType.newObject()
//                .name("queryType")
//                .field(GraphQLFieldDefinition.newFieldDefinition()
//                        .name("user")
//                        .type(user)
//                        .argument(newArgument().name("name")
//                                .type(Scalars.GraphQLString))
//                        .dataFetcher(userFetcher))
//                .build();
//
//        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(query).build();

        //init router
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(CookieHandler.create());
        router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.DELETE).allowedMethod(HttpMethod.OPTIONS).allowedHeader("Content-Type"));
        router.route("/user/*").handler(JWTAuthHandler.create(jwtAuth));
//        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        router.get("/user").handler(userController::findOneUserByName);
        router.post("/api/login").handler(userController::login);
        router.post("/api/signup").handler(userController::createOneUser);
        router.get("/api/projects/:userId").handler(projectController::findAllProjectsByUserId);
        router.delete("/api/project/:projectId/model/:modelId").handler(projectController::deleteModelByProjectIdAndModelId);
        router.get("/api/project/:projectId/models").handler(projectController::findModelsByProjectId);
        router.post("/api/project/:projectId/models").handler(projectController::insertModelByProjectId);
//        router.post("/graphql")
//                .handler(GraphQLPostHandler.create(graphQLSchema));

        //start server listening at portNumber
        int portnumber = Integer.valueOf(config().getString(SERVER_PORT, "8080"));
        server.requestHandler(router).listen(portnumber, httpServerAsyncResult -> {
            if (httpServerAsyncResult.succeeded()) {
                LOGGER.info("HTTP server running on port :{}", portnumber);
                startFuture.complete();
            } else {
                LOGGER.error("Could not start a HTTP server", httpServerAsyncResult.cause());
                startFuture.fail(httpServerAsyncResult.cause());
            }
        });
    }

}