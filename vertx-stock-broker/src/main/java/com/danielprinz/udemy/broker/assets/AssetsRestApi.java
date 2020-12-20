package com.danielprinz.udemy.broker.assets;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;

public class AssetsRestApi {

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");
  private static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);

  public static void attach(Router parent) {
    parent.get("/assets").handler(context -> {
      final JsonArray response = new JsonArray();
      ASSETS.stream().map(Asset::new).forEach(response::add);
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      // artificialSleep(context);
      context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .putHeader("my-header", "my-value")
        .end(response.toBuffer());
    });
  }

  /**
   * Used to showcase scaling & load testing
   * @param context routing context
   */
  private static void artificialSleep(final io.vertx.ext.web.RoutingContext context) {
    try {
      final int random = ThreadLocalRandom.current().nextInt(100, 300);
      if (random % 2 == 0) {
        Thread.sleep(random);
        context.response()
          .setStatusCode(500)
          .end("Sleeping...");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
