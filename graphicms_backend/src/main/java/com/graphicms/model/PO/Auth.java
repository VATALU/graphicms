package com.graphicms.model.PO;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter=true)
public class Auth {
    private String _id;
    private String auth;

    public Auth(JsonObject jsonObject) {
        this._id = jsonObject.getString("_id");
        this.auth = jsonObject.getString("auth");
    }

    public JsonObject toJson() {
        return new JsonObject().put("_id", _id).put("auth", auth);
    }

    public Auth() {
    }

    public Auth(String _id, String auth) {
        this._id = _id;
        this.auth = auth;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "_id='" + _id + '\'' +
                ", auth='" + auth + '\'' +
                '}';
    }
}