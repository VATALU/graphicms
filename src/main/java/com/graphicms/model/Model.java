package com.graphicms.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@DataObject
public class Model {
    private String _id;
    private String name;
    private String description;
    private String graphqlType;
    private JsonArray fields;

    public Model() {
    }

    public Model(String _id, String name, String description, String graphqlType, JsonArray fields) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.graphqlType = graphqlType;
        this.fields = fields;
    }

    public Model(JsonObject jsonObject) {
        this._id = jsonObject.getString("_id");
        this.name = jsonObject.getString("name");
        this.description = jsonObject.getString("description");
        this.graphqlType = jsonObject.getString("graphqlType");
        this.fields = jsonObject.getJsonArray("fields");
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("_id", _id)
                .put("name", name)
                .put("description", description)
                .put("graphqlType", graphqlType)
                .put("fields", fields);
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

    public JsonArray getFields() {
        return fields;
    }

    public void setFields(JsonArray fields) {
        this.fields = fields;
    }

    public String getGraphqlType() {
        return graphqlType;
    }

    public void setGraphqlType(String graphqlType) {
        this.graphqlType = graphqlType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Model{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", graphqlType='" + graphqlType + '\'' +
                ", fields=" + fields +
                '}';
    }
}