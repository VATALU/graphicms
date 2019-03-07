/*
* Copyright 2014 Red Hat, Inc.
*
* Red Hat licenses this file to you under the Apache License, version 2.0
* (the "License"); you may not use this file except in compliance with the
* License. You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/

package com.graphicms.service;

import com.graphicms.service.MongoService;
import io.vertx.core.Vertx;
import io.vertx.core.Handler;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.serviceproxy.ProxyHandler;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
import io.vertx.serviceproxy.HelperUtils;

import com.graphicms.service.MongoService;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.core.json.JsonArray;
import java.util.List;
import com.graphicms.model.PO.User;
import io.vertx.core.json.JsonObject;
import com.graphicms.model.PO.Project;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import com.graphicms.model.PO.Model;
/*
  Generated Proxy code - DO NOT EDIT
  @author Roger the Robot
*/

@SuppressWarnings({"unchecked", "rawtypes"})
public class MongoServiceVertxProxyHandler extends ProxyHandler {

  public static final long DEFAULT_CONNECTION_TIMEOUT = 5 * 60; // 5 minutes 
  private final Vertx vertx;
  private final MongoService service;
  private final long timerID;
  private long lastAccessed;
  private final long timeoutSeconds;

  public MongoServiceVertxProxyHandler(Vertx vertx, MongoService service){
    this(vertx, service, DEFAULT_CONNECTION_TIMEOUT);
  }

  public MongoServiceVertxProxyHandler(Vertx vertx, MongoService service, long timeoutInSecond){
    this(vertx, service, true, timeoutInSecond);
  }

  public MongoServiceVertxProxyHandler(Vertx vertx, MongoService service, boolean topLevel, long timeoutSeconds) {
      this.vertx = vertx;
      this.service = service;
      this.timeoutSeconds = timeoutSeconds;
      try {
        this.vertx.eventBus().registerDefaultCodec(ServiceException.class,
            new ServiceExceptionMessageCodec());
      } catch (IllegalStateException ex) {}
      if (timeoutSeconds != -1 && !topLevel) {
        long period = timeoutSeconds * 1000 / 2;
        if (period > 10000) {
          period = 10000;
        }
        this.timerID = vertx.setPeriodic(period, this::checkTimedOut);
      } else {
        this.timerID = -1;
      }
      accessed();
    }


  private void checkTimedOut(long id) {
    long now = System.nanoTime();
    if (now - lastAccessed > timeoutSeconds * 1000000000) {
      close();
    }
  }

    @Override
    public void close() {
      if (timerID != -1) {
        vertx.cancelTimer(timerID);
      }
      super.close();
    }

    private void accessed() {
      this.lastAccessed = System.nanoTime();
    }

  public void handle(Message<JsonObject> msg) {
    try{
      JsonObject json = msg.body();
      String action = msg.headers().get("action");
      if (action == null) throw new IllegalStateException("action not specified");
      accessed();
      switch (action) {
        case "findUserByName": {
          service.findUserByName((java.lang.String)json.getValue("name"),
                        res -> {
                        if (res.failed()) {
                          if (res.cause() instanceof ServiceException) {
                            msg.reply(res.cause());
                          } else {
                            msg.reply(new ServiceException(-1, res.cause().getMessage()));
                          }
                        } else {
                          msg.reply(res.result() == null ? null : res.result().toJson());
                        }
                     });
          break;
        }
        case "findUserByUserId": {
          service.findUserByUserId((java.lang.String)json.getValue("userId"),
                        res -> {
                        if (res.failed()) {
                          if (res.cause() instanceof ServiceException) {
                            msg.reply(res.cause());
                          } else {
                            msg.reply(new ServiceException(-1, res.cause().getMessage()));
                          }
                        } else {
                          msg.reply(res.result() == null ? null : res.result().toJson());
                        }
                     });
          break;
        }
        case "createUser": {
          service.createUser((java.lang.String)json.getValue("name"),
                        (java.lang.String)json.getValue("email"),
                        (java.lang.String)json.getValue("password"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findOwnersByProjectId": {
          service.findOwnersByProjectId((java.lang.String)json.getValue("projectId"),
                        HelperUtils.createListHandler(msg));
          break;
        }
        case "findAllProjectsByUserId": {
          service.findAllProjectsByUserId((java.lang.String)json.getValue("userId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findAllProjectInfosByUserId": {
          service.findAllProjectInfosByUserId((java.lang.String)json.getValue("userId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findModelsByProjectId": {
          service.findModelsByProjectId((java.lang.String)json.getValue("projectId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findModelByProjectIdAndModelId": {
          service.findModelByProjectIdAndModelId((java.lang.String)json.getValue("projectId"),
                        (java.lang.String)json.getValue("modelId"),
                        res -> {
                        if (res.failed()) {
                          if (res.cause() instanceof ServiceException) {
                            msg.reply(res.cause());
                          } else {
                            msg.reply(new ServiceException(-1, res.cause().getMessage()));
                          }
                        } else {
                          msg.reply(res.result() == null ? null : res.result().toJson());
                        }
                     });
          break;
        }
        case "insertModelsByProjectId": {
          service.insertModelsByProjectId((java.lang.String)json.getValue("projectId"),
                        json.getJsonObject("model") == null ? null : new com.graphicms.model.PO.Model(json.getJsonObject("model")),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "deleteModelByProjectIdAndModelId": {
          service.deleteModelByProjectIdAndModelId((java.lang.String)json.getValue("projectId"),
                        (java.lang.String)json.getValue("modelId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "createFieldByProjectIdAndModelId": {
          service.createFieldByProjectIdAndModelId((java.lang.String)json.getValue("projectId"),
                        (java.lang.String)json.getValue("modelId"),
                        (io.vertx.core.json.JsonObject)json.getValue("field"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "deleteField": {
          service.deleteField((java.lang.String)json.getValue("projectId"),
                        (java.lang.String)json.getValue("modelId"),
                        (java.lang.String)json.getValue("fieldName"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findContentByModels": {
          service.findContentByModels((java.lang.String)json.getValue("modelId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "graphql": {
          service.graphql((java.lang.String)json.getValue("modelId"),
                        (java.lang.String)json.getValue("graphqlQuery"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findModelByModelId": {
          service.findModelByModelId((java.lang.String)json.getValue("modelId"),
                        res -> {
                        if (res.failed()) {
                          if (res.cause() instanceof ServiceException) {
                            msg.reply(res.cause());
                          } else {
                            msg.reply(new ServiceException(-1, res.cause().getMessage()));
                          }
                        } else {
                          msg.reply(res.result() == null ? null : res.result().toJson());
                        }
                     });
          break;
        }
        case "qraphqlQuery": {
          service.qraphqlQuery((java.lang.String)json.getValue("collection"),
                        (io.vertx.core.json.JsonObject)json.getValue("arguments"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "graphqlMutation": {
          service.graphqlMutation((java.lang.String)json.getValue("collection"),
                        (io.vertx.core.json.JsonObject)json.getValue("arguments"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "deleteDocumentByItemId": {
          service.deleteDocumentByItemId((java.lang.String)json.getValue("collection"),
                        (java.lang.String)json.getValue("modelId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "createProjectByUserId": {
          service.createProjectByUserId((java.lang.String)json.getValue("userId"),
                        (java.lang.String)json.getValue("auth"),
                        json.getJsonObject("project") == null ? null : new com.graphicms.model.PO.Project(json.getJsonObject("project")),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "deleteProjectByUserIdAndProjectId": {
          service.deleteProjectByUserIdAndProjectId((java.lang.String)json.getValue("userId"),
                        (java.lang.String)json.getValue("projectId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "updateProjectAuthByUserIdAndProjectId": {
          service.updateProjectAuthByUserIdAndProjectId((java.lang.String)json.getValue("userId"),
                        (java.lang.String)json.getValue("projectId"),
                        (java.lang.String)json.getValue("auth"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findUserByUserName": {
          service.findUserByUserName((java.lang.String)json.getValue("userName"),
                        HelperUtils.createListHandler(msg));
          break;
        }
        case "insertAuthByUserName": {
          service.insertAuthByUserName((java.lang.String)json.getValue("userName"),
                        (java.lang.String)json.getValue("projectId"),
                        (java.lang.String)json.getValue("auth"),
                        HelperUtils.createHandler(msg));
          break;
        }
        case "findModelSizeByModelId": {
          service.findModelSizeByModelId((java.lang.String)json.getValue("projectId"),
                        HelperUtils.createHandler(msg));
          break;
        }
        default: throw new IllegalStateException("Invalid action: " + action);
      }
    } catch (Throwable t) {
      msg.reply(new ServiceException(500, t.getMessage()));
      throw t;
    }
  }
}