package com.danielprinz.udemy.vertx_starter.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class WorkerVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WorkerVerticle.class);

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    LOG.debug("Deployed as worker verticle.");
    startPromise.complete();
    Thread.sleep(5000);
    LOG.debug("Blocking operation done.");
  }
}
