package com.danielprinz.udemy.broker.watchlist;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class WatchListRestApi {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);

  public static void attach(final Router parent) {
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    final String path = "/account/watchlist/:accountId";
    parent.get(path).handler(context -> {
      String accountId = getAccountId(context);
      var watchList = Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));
      if (watchList.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "watchlist for account " + accountId + " not available!")
            .put("path", context.normalizedPath())
            .toBuffer()
          );
        return;
      }
      context.response().end(watchList.get().toJsonObject().toBuffer());
    });
    parent.put(path).handler(context -> {
      String accountId = getAccountId(context);

      var json = context.getBodyAsJson();
      var watchList = json.mapTo(WatchList.class);
      watchListPerAccount.put(UUID.fromString(accountId), watchList);
      context.response().end(json.toBuffer());
    });
    parent.delete(path).handler(context -> {
      String accountId = getAccountId(context);
      final WatchList deleted = watchListPerAccount.remove(UUID.fromString(accountId));
      LOG.info("Deleted: {}, Remaining: {}", deleted, watchListPerAccount.values());
      context.response()
        .end(deleted.toJsonObject().toBuffer());
    });
  }

  private static String getAccountId(final RoutingContext context) {
    var accountId = context.pathParam("accountId");
    LOG.debug("{} for account {}", context.normalizedPath(), accountId);
    return accountId;
  }

}
