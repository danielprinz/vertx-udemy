package com.danielprinz.udemy.vertx_websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer()
      .webSocketHandler(new WebSocketHandler(vertx))
      .listen(8900, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("HTTP server started on port 8900");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
