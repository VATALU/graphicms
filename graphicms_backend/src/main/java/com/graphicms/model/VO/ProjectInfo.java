package com.graphicms.model.VO;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class ProjectInfo {
    private String auth;
    private String _id;
    private String name;
    private String description;

    public ProjectInfo(String auth, String _id, String name, String description) {
        this.auth = auth;
        this._id = _id;
        this.name = name;
        this.description = description;
    }

    public ProjectInfo(JsonObject jsonObject) {
        ProjectInfoConverter.fromJson(jsonObject, this);
    }

    public ProjectInfo() {
    }

    public JsonObject toJson(ProjectInfo projectInfo) {
        JsonObject json = new JsonObject();
        ProjectInfoConverter.toJson(this, json);
        return json;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" + "auth='" + auth + '\'' + ", _id='" + _id + '\'' + ", name='" + name + '\''
                + ", description='" + description + '\'' + '}';
    }
}