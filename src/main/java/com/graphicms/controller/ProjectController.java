package com.graphicms.controller;

import com.graphicms.model.Model;
import com.graphicms.service.MongoService;
import com.graphicms.util.Api;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ProjectController {
    private final MongoService mongoService;

    public ProjectController(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public void findAllProjectsByUserId(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("userId");
        mongoService.findAllProjectsByUserId(userId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "projects", res.result());
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void findModelsByProjectId(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        mongoService.findModelsByProjectId(projectId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "models", res.result());
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void insertModelByProjectId(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        JsonObject body = routingContext.getBodyAsJson();
        Model model = new Model(body);
        System.out.println(model);
        mongoService.insertModelsByProjectId(projectId,model,res->{
            if(res.succeeded()) {
                Api.response(routingContext,200);
            } else {
                Api.failure(routingContext,200,res.cause().getMessage());
            }
        });
    }

}