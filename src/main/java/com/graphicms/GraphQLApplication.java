package com.graphicms;

import com.graphicms.util.PropertiesUtil;
import com.graphicms.verticle.StartVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class GraphQLApplication {
    public static void main(String[] args) {
        Vertx vertx =  Vertx.vertx();
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        JsonObject config = new JsonObject(PropertiesUtil.getConfig("config.properties"));
        deploymentOptions.setConfig(config);
        vertx.deployVerticle(new StartVerticle(), deploymentOptions);
    }
}