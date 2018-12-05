package com.graphicms.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class User {
    private String _id;
    private String name;
    private String password;

    public User() {
        this._id = "000000";
        this.name = "default_name";
        this.password = "123456";
    }

    public User(JsonObject json) {
        this._id = json.getString("_id");
        this.name = json.getString("name");
        this.password = json.getString("password");
    }

    public JsonObject toJson() {
        return new JsonObject().put("_id", _id)
                .put("name", name)
                .put("password", password);
    }

    public User(String _id, String name, String password) {
        this._id = _id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}