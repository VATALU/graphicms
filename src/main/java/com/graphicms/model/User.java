package com.graphicms.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class User {
    private String _id;
    private String name;
    private String password;
    private String email;

    public User() {
    }

    public User(JsonObject json) {
        this._id = json.getString("_id");
        this.name = json.getString("name");
        this.password = json.getString("password");
        this.email = json.getString("email");
    }

    public JsonObject toJson() {
        return new JsonObject().put("_id", _id)
                .put("name", name)
                .put("password", password)
                .put("email", email);
    }

    public User(String _id, String name, String password, String email) {
        this._id = _id;
        this.name = name;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}