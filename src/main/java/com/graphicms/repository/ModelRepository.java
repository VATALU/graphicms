package com.graphicms.repository;

import com.graphicms.model.Model;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface ModelRepository {
    void insertModelByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> asyncResultHandler);
}