package com.graphicms.util;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.CompletableFuture;

public class AsyncHandler<T> extends CompletableFuture<T> implements Handler<AsyncResult<T>> {
    @Override
    public void handle(final AsyncResult<T> tAsyncResult) {
        if(tAsyncResult.succeeded()) {
            complete(tAsyncResult.result());
        } else {
            completeExceptionally(tAsyncResult.cause());
        }
    }
}