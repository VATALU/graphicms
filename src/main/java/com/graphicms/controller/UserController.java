package com.graphicms.controller;

import com.graphicms.model.PO.User;
import com.graphicms.service.MongoService;
import com.graphicms.util.Api;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

import java.util.Objects;

public class UserController {

    private final MongoService mongoService;
    private final JWTAuth jwtAuth;

    public UserController(MongoService mongoService, JWTAuth jwtAuth) {
        this.mongoService = mongoService;
        this.jwtAuth = jwtAuth;
    }

    public void login(RoutingContext routingContext) {
        JsonObject body = routingContext.getBodyAsJson();
        String name = body.getString("username");
        String password = body.getString("password");
        mongoService.findUserByName(name, res -> {
            if (res.succeeded()) {
                User user = res.result();
                if (Objects.equals(password, user.getPassword())) {
                    String token = jwtAuth.generateToken(new JsonObject());
                    JsonObject data = new JsonObject().put("token", token).put("id", user.get_id());
                    Api.response(routingContext, 200, "data", data);
                } else {
                    Api.failure(routingContext, 200, "Password Error");
                }
            } else {
                Api.failure(routingContext, 200, "Invalid User");
            }
        });
    }

    public void findOneUserByName(RoutingContext routingContext) {
        String name = routingContext.request().getParam("username");
        mongoService.findUserByName(name, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "data", res.result());
            } else {
                Api.failure(routingContext, res.cause());
            }
        });
    }

    public void findOneUserByUserId(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("userId");
        mongoService.findUserByUserId(userId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "data", res.result().toJson());
            } else {
                Api.failure(routingContext, res.cause());
            }
        });
    }

    public void createOneUser(RoutingContext routingContext) {
        JsonObject body = routingContext.getBodyAsJson();
        String name = body.getString("username");
        String password = body.getString("password");
        String email = body.getString("email");
        Handler<AsyncResult<User>> handler = res -> {
            if (res.succeeded()) {
                Api.failure(routingContext, 200, "Duplicated Username");
            } else {
                mongoService.createUser(name, email, password, r -> {
                    if (r.succeeded()) {
                        User user = res.result();
                        String token = jwtAuth.generateToken(new JsonObject());
                        JsonObject data = new JsonObject().put("token", token).put("id", user.get_id());
                        Api.response(routingContext, 200, "data", data);
                    } else {
                        Api.failure(routingContext, r.cause());
                    }
                });
            }
        };
        mongoService.findUserByName(name, handler);
    }

    public void findOwnersByProjectId(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        mongoService.findOwnersByProjectId(projectId,res->{
            if(res.succeeded()) {
                Api.response(routingContext,200,"data",res.result());
            } else {
                Api.failure(routingContext,200);
            }
        });
    }
}