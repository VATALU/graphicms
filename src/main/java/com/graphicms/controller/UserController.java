package com.graphicms.controller;

import com.graphicms.service.UserService;
import com.graphicms.util.Api;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
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
        String name = body.getString("name");
        String password = body.getString("password");
        userService.findOneByName(name, res -> {
            if (res.succeeded()) {
                if (password.equals(res.result().getPassword())) {
                    Api.response(routingContext, 200, "token", jwtAuth.generateToken(new JsonObject()));
                } else {
                    Api.failure(routingContext, 500);
                }
            } else {
                Api.failure(routingContext, 500);
            }
        });
    }

    public void findOneUserByName(RoutingContext routingContext) {
        String name = routingContext.request().getParam("name");
        userService.findOneByName(name, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "data", res.result());
            } else {
                Api.failure(routingContext, res.cause());
            }
        });
    }


    public void logout(RoutingContext routingContext) {
    }

}