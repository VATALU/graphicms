package com.graphicms.repository;

import com.graphicms.model.PO.Model;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface ModelRepository {
    void insertModelByProjectId(String projectId, Model model, Handler<AsyncResult<Void>> resultHandler);

    void deleteModelByProjectIdAndModelId(String projectId, String modelId, Handler<AsyncResult<Void>> resultHandler);
}