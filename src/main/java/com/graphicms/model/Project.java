package com.graphicms.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@DataObject
public class Project {
    private String _id;
    private String name;
    private String description;
    private JsonArray models;

    public Project(JsonObject jsonObject) {
        this._id = jsonObject.getString("_id");
        this.name = jsonObject.getString("name");
        this.description = jsonObject.getString("description");
        this.models = jsonObject.getJsonArray("fields");
    }

    public Project() {

    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("_id", _id)
                .put("name", name)
                .put("description", description)
                .put("schemas", models);
    }

    public Project(String _id, String name, String description, JsonArray models) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.models = models;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public JsonArray getModels() {
        return models;
    }

    public void setModels(JsonArray models) {
        this.models = models;
    }

    @Override
    public String toString() {
        return "Project{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", schemas=" + models +
                '}';
    }
}