package com.graphicms.graphQL.queryExecutor;

import java.util.Map;


import com.graphicms.graphQL.queryExecutor.impl.AsyncGraphQLExecImpl;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface AsyncGraphQLExec {
	
	public static AsyncGraphQLExec create(GraphQL.Builder builder) {
		return new AsyncGraphQLExecImpl(builder);
	}
	
	public static AsyncGraphQLExec create(GraphQLSchema schema) {
		return new AsyncGraphQLExecImpl(GraphQL.newGraphQL(schema));
	}

	public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables);

}
