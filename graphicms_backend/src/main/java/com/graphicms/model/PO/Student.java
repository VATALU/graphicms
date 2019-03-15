package com.graphicms.model.PO;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter=true)
public class Student {
    private String Name;

    public Student(){};

    public Student(JsonObject json) {
        this.Name=json.getString("Name");
    }

    public JsonObject toJson() {
        return new JsonObject().put("Name",this.getName());
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}