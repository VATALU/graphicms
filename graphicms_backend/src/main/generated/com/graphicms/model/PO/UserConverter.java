package com.graphicms.model.PO;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.graphicms.model.PO.User}.
 * NOTE: This class has been automatically generated from the {@link com.graphicms.model.PO.User} original class using Vert.x codegen.
 */
public class UserConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, User obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "avatar":
          if (member.getValue() instanceof String) {
            obj.setAvatar((String)member.getValue());
          }
          break;
        case "email":
          if (member.getValue() instanceof String) {
            obj.setEmail((String)member.getValue());
          }
          break;
        case "groups":
          if (member.getValue() instanceof JsonArray) {
            obj.setGroups(((JsonArray)member.getValue()).copy());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "password":
          if (member.getValue() instanceof String) {
            obj.setPassword((String)member.getValue());
          }
          break;
        case "projects":
          if (member.getValue() instanceof JsonArray) {
            obj.setProjects(((JsonArray)member.getValue()).copy());
          }
          break;
      }
    }
  }

  public static void toJson(User obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(User obj, java.util.Map<String, Object> json) {
    if (obj.getAvatar() != null) {
      json.put("avatar", obj.getAvatar());
    }
    if (obj.getEmail() != null) {
      json.put("email", obj.getEmail());
    }
    if (obj.getGroups() != null) {
      json.put("groups", obj.getGroups());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
    if (obj.getProjects() != null) {
      json.put("projects", obj.getProjects());
    }
  }
}
