package com.graphicms.controller;

import com.graphicms.repository.ProjectRepository;
import com.graphicms.service.MongoService;
import com.graphicms.util.Api;
import io.vertx.ext.web.RoutingContext;

public class ProjectController {
    private final MongoService mongoService;

    public ProjectController(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public void findAllProjecrsByUserId(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("userId");
        mongoService.findAllProjectsByUserId(userId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "projects", res.result());
            } else {
                Api.failure(routingContext, 200);
            }
        });
    }
}