package com.graphicms.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

@DataObject
public class Project {
    private String _id;
    private String name;
    private String description;
    private String group;
    private List<Schema> schemas;

    public Project(JsonObject jsonObject) {
        this._id = jsonObject.getString("_id");
        this.name = jsonObject.getString("name");
        this.description = jsonObject.getString("description");
        this.group = jsonObject.getString("group");
        JsonArray array = jsonObject.getJsonArray("fields");
        for (int i = 0; i < array.size(); i++) {
            this.schemas.add(array.getJsonObject(i).mapTo(Schema.class));
        }
    }

    public Project() {

    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("_id", _id)
                .put("name", name)
                .put("description", description)
                .put("group", group)
                .put("schemas", new JsonArray(schemas));
    }

    public Project(String _id, String name, String description, String group, List<Schema> schemas) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.group = group;
        this.schemas = schemas;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Schema> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<Schema> schemas) {
        this.schemas = schemas;
    }

    @Override
    public String toString() {
        return "Project{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", group='" + group + '\'' +
                ", schemas=" + schemas +
                '}';
    }
}