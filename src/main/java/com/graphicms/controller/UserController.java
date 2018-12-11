package com.graphicms.controller;

import com.graphicms.model.User;
import com.graphicms.service.UserService;
import com.graphicms.util.Api;
import com.graphicms.util.AsyncHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

public class UserController {

    private final UserService userService;
    private final JWTAuth jwtAuth;

    public UserController(UserService userService, JWTAuth jwtAuth) {
        this.userService = userService;
        this.jwtAuth = jwtAuth;
    }

    public void login(RoutingContext routingContext) {
        JsonObject body = routingContext.getBodyAsJson();
        String name = body.getString("username");
        String password = body.getString("password");
        userService.findOneByName(name, res -> {
            if (res.succeeded()) {
                if (password.equals(res.result().getPassword())) {
                    Api.response(routingContext, 200, "token", jwtAuth.generateToken(new JsonObject()));
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
        userService.findOneByName(name, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "data", res.result());
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
                userService.insert(name, email, password, r -> {
                    if (r.succeeded()) {
                        Api.response(routingContext, 200, "data", r.result());
                    } else {
                        Api.failure(routingContext, r.cause());
                    }
                });
            }
        };
        userService.findOneByName(name, handler);

    }
}