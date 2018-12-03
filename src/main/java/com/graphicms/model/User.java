package com.graphicms.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class User {
    private String _id;
    private String name;

    public User() {
        this._id="123";
        this.name="gxy";
    }

    public User(JsonObject json) {
        this._id=json.getString("_id");
        this.name =json.getString("name");
    }

    public JsonObject toJson() {
        return new JsonObject() .put("_id", _id)
                .put("name", name);
    }

    public User(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}