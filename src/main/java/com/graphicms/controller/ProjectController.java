package com.graphicms.controller;

import com.graphicms.model.PO.Model;
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
        mongoService.findAllProjectInfosByUserId(userId, res -> {
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

    public void findModelByProjectIdAndModelId(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        String modelId = routingContext.request().getParam("modelId");
        mongoService.findModelByProjectIdAndModelId(projectId, modelId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "model", res.result().toJson());
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void insertModelByProjectId(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        JsonObject body = routingContext.getBodyAsJson();
        Model model = new Model(body);
        mongoService.insertModelsByProjectId(projectId, model, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "_id", res.result());
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void deleteModelByProjectIdAndModelId(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        String modelId = routingContext.request().getParam("modelId");
        mongoService.deleteModelByProjectIdAndModelId(projectId, modelId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200);
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void createField(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        String modelId = routingContext.request().getParam("modelId");
        JsonObject fieldJson = routingContext.getBodyAsJson();
        mongoService.createFieldByProjectIdAndModelId(projectId, modelId, fieldJson, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200);
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void deleteField(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        String modelId = routingContext.request().getParam("modelId");
        String fieldName = routingContext.request().getParam("fieldName");
        mongoService.deleteField(projectId, modelId, fieldName, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200);
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void findContent(RoutingContext routingContext) {
        String modelId = routingContext.request().getParam("modelId");
        mongoService.findContentByModels(modelId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200, "data", res.result());
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }

    public void deleteDocumentByItemId(RoutingContext routingContext) {
        String modelId = routingContext.request().getParam("modelId");
        String itemId = routingContext.request().getParam("itemId");
        mongoService.deleteDocumentByItemId(modelId, itemId, res -> {
            if (res.succeeded()) {
                Api.response(routingContext, 200);
            } else {
                Api.failure(routingContext, 200, res.cause().getMessage());
            }
        });
    }
}