package com.graphicms.model.PO;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.graphicms.model.PO.Project}.
 * NOTE: This class has been automatically generated from the {@link com.graphicms.model.PO.Project} original class using Vert.x codegen.
 */
public class ProjectConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Project obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "description":
          if (member.getValue() instanceof String) {
            obj.setDescription((String)member.getValue());
          }
          break;
        case "models":
          if (member.getValue() instanceof JsonArray) {
            obj.setModels(((JsonArray)member.getValue()).copy());
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

  public static void toJson(Project obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Project obj, java.util.Map<String, Object> json) {
    if (obj.getDescription() != null) {
      json.put("description", obj.getDescription());
    }
    if (obj.getModels() != null) {
      json.put("models", obj.getModels());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }
}
