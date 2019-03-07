package com.graphicms.verticle;

import com.graphicms.service.MongoService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.serviceproxy.ServiceBinder;

public class MongoDBVerticle extends AbstractVerticle {

    private static final String MONGO_URI = "mongo.uri";
    private static final String MONGO_DB = "mongo.db";
    private static final String MONGO_ADDRESS = "address.mongo";

    @Override
    public void start(Future<Void> startFuture) {
        String uri = config().getString(MONGO_URI);
        String db = config().getString(MONGO_DB);
        JsonObject mongoConfig = new JsonObject().put("connect_string", uri).put("db_name", db);
        MongoClient mongoClient = MongoClient.createShared(vertx, mongoConfig);
        MongoService.create(mongoClient, res -> {
            if (res.succeeded()) {
                ServiceBinder serviceBinder = new ServiceBinder(vertx);
                serviceBinder.setAddress(config().getString(MONGO_ADDRESS)).register(MongoService.class, res.result());
                startFuture.complete();
            } else {
                startFuture.fail(res.cause());
            }
        });
    }
}