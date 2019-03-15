package com.graphicms.model.PO;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.graphicms.model.PO.Field}.
 * NOTE: This class has been automatically generated from the {@link com.graphicms.model.PO.Field} original class using Vert.x codegen.
 */
public class FieldConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Field obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "type":
          if (member.getValue() instanceof String) {
            obj.setType((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Field obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Field obj, java.util.Map<String, Object> json) {
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType());
    }
  }
}
