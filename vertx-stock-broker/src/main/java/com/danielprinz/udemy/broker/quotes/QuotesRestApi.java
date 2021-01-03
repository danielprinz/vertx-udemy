package com.danielprinz.udemy.broker.quotes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.danielprinz.udemy.broker.assets.Asset;
import com.danielprinz.udemy.broker.assets.AssetsRestApi;

import io.vertx.ext.web.Router;
import io.vertx.sqlclient.Pool;

public class QuotesRestApi {

  public static void attach(Router parent, final Pool db) {
    final Map<String, Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(symbol ->
      cachedQuotes.put(symbol, initRandomQuote(symbol))
    );
    parent.get("/quotes/:asset").handler(new GetQuoteHandler(cachedQuotes));
    parent.get("/pg/quotes/:asset").handler(new GetQuoteFromDatabaseHandler(db));
  }

  private static Quote initRandomQuote(final String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
