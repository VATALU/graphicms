package com.graphicms.model.VO;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.graphicms.model.VO.ProjectInfo}.
 * NOTE: This class has been automatically generated from the {@link com.graphicms.model.VO.ProjectInfo} original class using Vert.x codegen.
 */
public class ProjectInfoConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ProjectInfo obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "auth":
          if (member.getValue() instanceof String) {
            obj.setAuth((String)member.getValue());
          }
          break;
        case "description":
          if (member.getValue() instanceof String) {
            obj.setDescription((String)member.getValue());
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

  public static void toJson(ProjectInfo obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ProjectInfo obj, java.util.Map<String, Object> json) {
    if (obj.getAuth() != null) {
      json.put("auth", obj.getAuth());
    }
    if (obj.getDescription() != null) {
      json.put("description", obj.getDescription());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }
}
