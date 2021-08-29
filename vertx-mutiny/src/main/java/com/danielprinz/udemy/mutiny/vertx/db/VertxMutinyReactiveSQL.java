package com.danielprinz.udemy.mutiny.vertx.db;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;


public class VertxMutinyReactiveSQL extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VertxMutinyReactiveSQL.class);
  private static final int HTTP_SERVER_PORT = 8111;

  public static void main(String[] args) {
    var port = EmbeddedPostgres.startPostgres();

    final var options = new DeploymentOptions()
      .setConfig(new JsonObject().put("port", port));
    Vertx.vertx().deployVerticle(VertxMutinyReactiveSQL::new, options)
      .subscribe().with(id -> LOG.info("Started: {}", id));
  }

  @Override
  public Uni<Void> asyncStart() {
    vertx.periodicStream(5000L).toMulti()
      .subscribe().with(item -> LOG.info("{}", LocalTime.now().getMinute()));
    var db = createPgPool(config());

    var router = Router.router(vertx);
    router.route().failureHandler(this::failureHandler);
    router.get("/users").respond(context ->
      executeQuery(db)
    );

    return vertx.createHttpServer()
      .requestHandler(router)
      .listen(HTTP_SERVER_PORT)
      .replaceWithVoid();
  }

  private Uni<JsonArray> executeQuery(Pool db) {
    LOG.info("Executing DB query to find all users...");
    return db.query("SELECT * from users")
      .execute()
      .onItem().transform(rows -> {
        var data = new JsonArray();
        for (Row row : rows) {
          data.add(row.toJson());
        }
        LOG.info("Return data: {}", data);
        return data;
      })
      .onFailure()
      .invoke(failure -> LOG.error("Failed query: ", failure))
      .onFailure()
      .recoverWithItem(new JsonArray());
  }

  private Pool createPgPool(JsonObject config) {
    var connectOptions = new PgConnectOptions()
      .setHost("localhost")
      .setPort(config.getInteger("port"))
      .setDatabase(EmbeddedPostgres.DATABASE_NAME)
      .setUser(EmbeddedPostgres.USERNAME)
      .setPassword(EmbeddedPostgres.PASSWORD);

    var poolOptions = new PoolOptions().setMaxSize(5);

    return PgPool.pool(vertx, connectOptions, poolOptions);
  }

  private void failureHandler(RoutingContext routingContext) {
    routingContext.response().setStatusCode(500).endAndForget("Something went wrong :(");
  }

}
