package com.graphicms.graphQL;

import com.graphicms.graphQL.queryExecutor.AsyncExecutionException;
import com.graphicms.graphQL.queryExecutor.AsyncGraphQLExec;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GraphQLPostHandler {
    public static Handler<RoutingContext> create(GraphQLSchema graphQLSchema) {
        return create(GraphQL.newGraphQL(graphQLSchema));
    }

    public static Handler<RoutingContext> create(GraphQL.Builder builder){
        AsyncGraphQLExec asyncGraphQLExec = AsyncGraphQLExec.create(builder);

        // get the body and parse
        return routingContext -> {
            try {
                JsonObject bodyJson = routingContext.getBodyAsJson();
                String query = bodyJson.getString("query");
                JsonObject bodyVariables = bodyJson.getJsonObject("variables");
                String operationName = bodyJson.getString("operationName");
                Map<String, Object> variables;
                if (bodyVariables == null || bodyVariables.isEmpty()) {
                    variables = new HashMap<>();
                } else {
                    variables = bodyVariables.getMap();
                }

                // execute the graphql query
                asyncGraphQLExec.executeQuery(query, operationName, routingContext, variables).setHandler(queryResult -> {
                    if (queryResult.succeeded()) {
                        JsonObject json = queryResult.result();
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
            } catch (Exception e) {
                Map<String, Object> res = new HashMap<>();
                res.put("errors", Arrays.asList(e));
                JsonObject errorResult = new JsonObject(res);
                routingContext.response().setStatusCode(400).end(errorResult.encode());
            }
        };
    }
}