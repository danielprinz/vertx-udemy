package com.danielprinz.udemy.ping_pong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class PongVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    System.out.println("Pong - Running on event loop: " + Thread.currentThread().getName());
    vertx.eventBus().consumer("ping-pong", message -> {
      System.out.println(message.body() + " on thread: " + Thread.currentThread().getName());
      message.reply(new JsonObject().put("msg", "pong"));
    });
  }
}
