package com.graphicms.model.PO;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.graphicms.model.PO.Auth}.
 * NOTE: This class has been automatically generated from the {@link com.graphicms.model.PO.Auth} original class using Vert.x codegen.
 */
public class AuthConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Auth obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "auth":
          if (member.getValue() instanceof String) {
            obj.setAuth((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Auth obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Auth obj, java.util.Map<String, Object> json) {
    if (obj.getAuth() != null) {
      json.put("auth", obj.getAuth());
    }
  }
}
