package com.graphicms.model.VO;

import io.vertx.core.json.JsonObject;

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
        this.auth=jsonObject.getString("auth");
        this._id=jsonObject.getString("_id");
        this.name=jsonObject.getString("name");
        this.description=jsonObject.getString("description");
    }

    public ProjectInfo() {
    }

    public JsonObject toJson(ProjectInfo projectInfo) {
        return new JsonObject()
                .put("auth",projectInfo.getAuth())
                .put("_id",projectInfo.get_id())
                .put("name",projectInfo.getName())
                .put("description",projectInfo.getDescription());
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
        return "ProjectInfo{" +
                "auth='" + auth + '\'' +
                ", _id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}