package com.danielprinz.udemy.ping_pong.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class TestCustomCodec {

  @Test
  void localCodecWorksCorrectly(Vertx vertx, VertxTestContext context) {
    vertx.eventBus().registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
    vertx.eventBus().registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));

    vertx.eventBus().<Ping>consumer("test", msg -> {
      assertEquals("ping", msg.body().getMessage());
      msg.reply(new Pong("pong"));
    });

    vertx.eventBus().<Pong>request("test", new Ping("ping"), reply -> {
      if (reply.failed()) {
        context.failNow(reply.cause());
      }
      assertEquals("pong", reply.result().body().getMessage());
      context.completeNow();
    });
  }
}
