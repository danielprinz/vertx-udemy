package com.danielprinz.udemy.broker.assets;

import java.util.Arrays;
import java.util.List;

import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgPool;

public class AssetsRestApi {

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");

  public static void attach(Router parent, final PgPool db) {
    parent.get("/assets").handler(new GetAssetsHandler());
    parent.get("/pg/assets").handler(new GetAssetsFromDatabaseHandler(db));
  }

}
