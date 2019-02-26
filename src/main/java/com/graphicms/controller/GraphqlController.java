package com.graphicms.controller;

import com.graphicms.graphQL.dataFetcher.AsyncDataFetcher;
import com.graphicms.graphQL.queryExecutor.AsyncExecutionException;
import com.graphicms.graphQL.queryExecutor.AsyncGraphQLExec;
import com.graphicms.model.CglibBean;
import com.graphicms.model.PO.Field;
import com.graphicms.model.PO.Model;
import com.graphicms.model.PO.Student;
import com.graphicms.service.MongoService;
import com.graphicms.util.Api;
import com.graphicms.util.StringUtil;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.*;

import static graphql.schema.GraphQLArgument.newArgument;

public class GraphqlController {
    private final MongoService mongoService;

    public GraphqlController(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public Handler<RoutingContext> graphql() {
        return routingContext -> {
            JsonObject body = routingContext.getBodyAsJson();
            String modelId = body.getString("modelId");
            String graphqlQuery = body.getString("query");
            mongoService.findModelByModelId(modelId, res1 -> {
                        if (res1.succeeded()) {
                            //define schema
                            System.out.println(res1.result());
                            GraphQLObjectType query = defineSchema(res1.result());
                            GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(query).build();
                            AsyncGraphQLExec asyncGraphQLExec = AsyncGraphQLExec.create(graphQLSchema);
                            asyncGraphQLExec.executeQuery(graphqlQuery, null, routingContext, null).setHandler(queryResult -> {
                                if (queryResult.succeeded()) {
                                    JsonObject json = queryResult.result();
                                    System.out.println(json);
                                    routingContext.response().end(new JsonObject().put("data", json).encode());
                                } else {
                                    Map<String, Object> res = new HashMap<>();
                                    res.put("data", null);
                                    if (queryResult.cause() instanceof AsyncExecutionException) {
                                        AsyncExecutionException ex = (AsyncExecutionException) queryResult.cause();
                                        res.put("errors", ex.getErrors());
                                    } else {
                                        res.put("errors", queryResult.cause() != null ? Arrays.asList(queryResult.cause()) : Arrays.asList(new Exception("Internal error")));
                                    }
                                    JsonObject errorResult = new JsonObject(res);
                                    routingContext.response().setStatusCode(400).end(errorResult.encode());
                                }
                            });
                        } else {
                            Api.failure(routingContext, 200);
                        }
                    }
            );
        };
    }


    private GraphQLObjectType defineSchema(Model model) {
        JsonArray fields = model.getFields();
        String modelName = model.getName();
        String modelId = model.get_id();
        Map<String, GraphQLScalarType> scalarsMap = new HashMap<String, GraphQLScalarType>() {{
            this.put("String", Scalars.GraphQLString);
            this.put("Int", Scalars.GraphQLInt);
        }};

        //type
        GraphQLObjectType.Builder typeBuilder = GraphQLObjectType.newObject().name(modelName);
        fields.forEach(field -> {
            Field f = new Field((JsonObject) field);
            typeBuilder.field(GraphQLFieldDefinition.newFieldDefinition()
                    //field 首字母小写
                    .name(StringUtil.toLowerCaseFirstOne(f.getName()))
                    .type(scalarsMap.get(f.getType())).build());
        });
        GraphQLObjectType type = typeBuilder.build();

        //datafetcher
        AsyncDataFetcher<Object> dataFetcher = (env, handler) -> {
            Map<String, Object> argumentsMap = env.getArguments();
            Map<String, Object> upperArgumentsMap = new HashMap<>();
            Set<Map.Entry<String, Object>> argumentsMapEntries = argumentsMap.entrySet();
            argumentsMapEntries.forEach(argumentsMapEntry -> {
                String key = StringUtil.toUpperCaseFirstOne(argumentsMapEntry.getKey());
                upperArgumentsMap.put(key, argumentsMapEntry.getValue());
            });

            JsonObject arguments = new JsonObject(upperArgumentsMap);
            mongoService.qraphqlQuery(modelId, arguments, res -> {
                if (res.succeeded()) {
                    JsonObject jsonObject = res.result();
                    HashMap<String, Class<?>> propertyMap = new HashMap<>();
                    Set<String> fieldNames = jsonObject.fieldNames();
                    fieldNames.forEach(fieldName -> {
                        propertyMap.put(StringUtil.toLowerCaseFirstOne(fieldName), jsonObject.getValue(fieldName).getClass());
                    });

                    CglibBean cglibBean = new CglibBean(propertyMap);
                    fieldNames.forEach(fieldName -> cglibBean.setValue(StringUtil.toLowerCaseFirstOne(fieldName), jsonObject.getValue(fieldName)));
                    Object object = cglibBean.getObject();
                    handler.handle(Future.succeededFuture(object));
                }
            });
        };
        //field
        GraphQLFieldDefinition.Builder fieldBuilder = GraphQLFieldDefinition.newFieldDefinition()
                .name(modelName)
                .type(type);
        fields.forEach(field -> {
            Field f = new Field((JsonObject) field);
            fieldBuilder.argument(newArgument().name(StringUtil.toLowerCaseFirstOne(f.getName())).type(scalarsMap.get(f.getType())).build());
        });
        fieldBuilder.dataFetcher(dataFetcher);
        GraphQLObjectType query = GraphQLObjectType.newObject()
                .name("Query")
                .field(fieldBuilder)
                .build();
        return query;
    }
}