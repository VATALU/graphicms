package com.graphicms.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

@DataObject
public class Schema {
    private String name;
    private String description;
    private String graphqlType;
    private List<Field> fields;

    public Schema() {
    }

    public Schema(String name, String description, String graphqlType, List<Field> fields) {
        this.name = name;
        this.description = description;
        this.graphqlType = graphqlType;
        this.fields = fields;
    }

    public Schema(JsonObject jsonObject) {
        this.name = jsonObject.getString("name");
        this.description = jsonObject.getString("description");
        this.graphqlType = jsonObject.getString("graphqlType");
        JsonArray fieldsJson = jsonObject.getJsonArray("fields");
        for (int i = 0; i < fieldsJson.size(); i++) {
            this.fields.add(fieldsJson.getJsonObject(i).mapTo(Field.class));
        }
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("name", name)
                .put("description", description)
                .put("graphqlType", graphqlType)
                .put("fields", new JsonArray(fields));
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

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getGraphqlType() {
        return graphqlType;
    }

    public void setGraphqlType(String graphqlType) {
        this.graphqlType = graphqlType;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", graphqlType='" + graphqlType + '\'' +
                ", fields=" + fields +
                '}';
    }
}