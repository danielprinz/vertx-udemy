package com.danielprinz.udemy.ping_pong;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class TestPongVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new PongVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Timeout(5000)
  @Test
  void pongIsReplied(Vertx vertx, VertxTestContext context) {
    vertx.eventBus().request("ping-pong", "ping-test", reply -> {
      if (reply.failed()) {
        context.failNow(reply.cause());
      } else {
        assertEquals(new JsonObject().put("msg", "pong"), reply.result().body());
        context.completeNow();
      }
    });
  }
}
