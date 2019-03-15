package com.graphicms.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * start verticle
 */
public class StartVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {
        Future<String> mongoVerticleFuture = Future.future();
        JsonObject config = config();
        DeploymentOptions deploymentOptions =  new DeploymentOptions().setConfig(config);
        vertx.deployVerticle(new MongoDBVerticle(),deploymentOptions, mongoVerticleFuture.completer());

        mongoVerticleFuture.compose(res -> {
            Future<String> httpVerticleFuture = Future.future();
            vertx.deployVerticle(new HttpServerVerticle(),deploymentOptions,httpVerticleFuture.completer());
            return httpVerticleFuture;
        }).setHandler(res -> {
            if(res.succeeded()) {
                startFuture.succeeded();
            } else {
                startFuture.fail(res.cause());
            }
        });
    }
}