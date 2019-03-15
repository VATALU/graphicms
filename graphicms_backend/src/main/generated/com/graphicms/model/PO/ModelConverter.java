package com.graphicms.model.PO;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.graphicms.model.PO.Model}.
 * NOTE: This class has been automatically generated from the {@link com.graphicms.model.PO.Model} original class using Vert.x codegen.
 */
public class ModelConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Model obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "description":
          if (member.getValue() instanceof String) {
            obj.setDescription((String)member.getValue());
          }
          break;
        case "fields":
          if (member.getValue() instanceof JsonArray) {
            obj.setFields(((JsonArray)member.getValue()).copy());
          }
          break;
        case "graphqlType":
          if (member.getValue() instanceof String) {
            obj.setGraphqlType((String)member.getValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Model obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Model obj, java.util.Map<String, Object> json) {
    if (obj.getDescription() != null) {
      json.put("description", obj.getDescription());
    }
    if (obj.getFields() != null) {
      json.put("fields", obj.getFields());
    }
    if (obj.getGraphqlType() != null) {
      json.put("graphqlType", obj.getGraphqlType());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }
}
