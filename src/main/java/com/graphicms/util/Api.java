package com.graphicms.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Api {

    public static void response(RoutingContext context, final int statusCode) {
        response(context, statusCode, null, null);
    }

    public static void response(RoutingContext context, final int statusCode, final String jsonField, Object jsonData) {
        context.response().setStatusCode(statusCode);
        context.response().putHeader("Content-Type", "application/json");
        JsonObject wrapped = new JsonObject().put("success", true);
        if (jsonField != null && jsonData != null) {
            wrapped.put(jsonField, jsonData);
        }
        context.response().end(wrapped.encode());
    }

    public static void failure(RoutingContext context, final int statusCode) {
        failure(context, statusCode, null);
    }

    public static void failure(RoutingContext context, final Throwable t) {
        failure(context, 500, t.getMessage());
    }

    public static void failure(RoutingContext context, final int statusCode, final String error) {
        context.response().setStatusCode(statusCode);
        context.response().putHeader("Content-Type", "application/json");
        JsonObject json = new JsonObject().put("success", false);
        if (error != null) {
            json.put("error", error);
        }
        context.response().end(json.encode());
    }

    private Api() {
    }
}