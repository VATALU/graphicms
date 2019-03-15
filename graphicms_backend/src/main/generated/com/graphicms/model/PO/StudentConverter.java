package com.graphicms.model.PO;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.graphicms.model.PO.Student}.
 * NOTE: This class has been automatically generated from the {@link com.graphicms.model.PO.Student} original class using Vert.x codegen.
 */
public class StudentConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Student obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Student obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Student obj, java.util.Map<String, Object> json) {
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }
}
