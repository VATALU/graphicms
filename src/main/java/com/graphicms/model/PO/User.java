package com.graphicms.model.PO;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

@DataObject
public class User {
    private String _id;
    private String name;
    private String password;
    private String email;
    private JsonArray groups;
    private JsonArray projects;

    public User() {
    }

    public User(JsonObject json) {
        this._id = json.getString("_id");
        this.name = json.getString("name");
        this.password = json.getString("password");
        this.email = json.getString("email");
        this.groups = json.getJsonArray("groups");
        this.projects = json.getJsonArray("projects");
    }

    public JsonObject toJson() {
        return new JsonObject().put("_id", _id)
                .put("name", name)
                .put("password", password)
                .put("email", email)
                .put("groups", groups)
                .put("projects", projects);
    }

    public User(String _id, String name, String password, String email, JsonArray groups, JsonArray projects) {
        this._id = _id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.groups = groups;
        this.projects = projects;
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

    public JsonArray getGroups() {
        return groups;
    }

    public void setGroups(JsonArray groups) {
        this.groups = groups;
    }

    public JsonArray getProjects() {
        return projects;
    }

    public void setProjects(JsonArray projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", groups=" + groups +
                ", projects=" + projects +
                '}';
    }
}