package com.graphicms.verticle;

import com.graphicms.graphQL.dataFetcher.AsyncDataFetcher;
import com.graphicms.model.User;
import com.graphicms.service.UserService;
import com.graphicms.graphQL.GraphQLPostHandler;
import graphql.*;
import graphql.schema.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static graphql.schema.GraphQLArgument.newArgument;

/**
 * server verticle
 */
public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private static final String SERVER_PORT = "server.port";
    private static final String MONGO_ADDRESS = "address.mongo";

    private UserService userService;

    @Override
    public void start(Future<Void> startFuture) {
        userService = UserService.createProxy(vertx, config().getString(MONGO_ADDRESS));

        //create server
        HttpServer server = vertx.createHttpServer();

        //set graphql
        AsyncDataFetcher<List<User>> userFetcher = (env, handler) -> {
            String name = env.getArgument("name");
            userService.findOneByName(name, handler);
        };

        GraphQLObjectType user = GraphQLObjectType.newObject()
                .name("user")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("_id")
                        .type(Scalars.GraphQLString)
                )
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("name")
                        .type(Scalars.GraphQLString)
                )
                .build();

        GraphQLObjectType query = GraphQLObjectType.newObject()
                .name("queryType")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("user")
                        .type(new GraphQLList(user))
                        .argument(newArgument().name("name")
                                .type(Scalars.GraphQLString))
                        .dataFetcher(userFetcher))
                .build();

        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(query).build();

        //init router
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        router.get("/user").handler(this::findOneUserByName);
        router.post("/graphql")
                .handler(GraphQLPostHandler.create(graphQLSchema));
        //start server listening at 8080
        int portnumber = Integer.valueOf(config().getString(SERVER_PORT, "8080"));
        server.requestHandler(router::accept).listen(portnumber, httpServerAsyncResult -> {
            if (httpServerAsyncResult.succeeded()) {
                LOGGER.info("HTTP server running on port :{}", portnumber);
                startFuture.complete();
            } else {
                LOGGER.error("Could not start a HTTP server", httpServerAsyncResult.cause());
                startFuture.fail(httpServerAsyncResult.cause());
            }
        });
    }

    private void findOneUserByName(RoutingContext routingContext) {
        String name = routingContext.request().getParam("name");
        userService.findOneByName(name, res -> {
            if (res.succeeded()) {
                apiResponse(routingContext, 200, "data", res.result());
            } else {
                apiFailure(routingContext, res.cause());
            }
        });
    }

    private void apiResponse(RoutingContext context, int statusCode, String jsonField, Object jsonData) {
        context.response().setStatusCode(statusCode);
        context.response().putHeader("Content-Type", "application/json");
        JsonObject wrapped = new JsonObject().put("success", true);
        if (jsonField != null && jsonData != null) {
            wrapped.put(jsonField, jsonData);
        }
        context.response().end(wrapped.encode());
    }

    private void apiFailure(RoutingContext context, Throwable t) {
        apiFailure(context, 500, t.getMessage());
    }

    private void apiFailure(RoutingContext context, int statusCode, String error) {
        context.response().setStatusCode(statusCode);
        context.response().putHeader("Content-Type", "application/json");
        context.response().end(new JsonObject()
                .put("success", false)
                .put("error", error).encode());
    }
}