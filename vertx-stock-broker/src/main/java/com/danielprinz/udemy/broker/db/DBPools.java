package com.danielprinz.udemy.broker.db;

import com.danielprinz.udemy.broker.config.BrokerConfig;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class DBPools {

  private DBPools() {}

  public static Pool createMySQLPool(final BrokerConfig configuration, final Vertx vertx) {
    final var connectOptions = new MySQLConnectOptions()
      .setHost(configuration.getDbConfig().getHost())
      .setPort(configuration.getDbConfig().getPort())
      .setDatabase(configuration.getDbConfig().getDatabase())
      .setUser(configuration.getDbConfig().getUser())
      .setPassword(configuration.getDbConfig().getPassword());

    var poolOptions = new PoolOptions()
      .setMaxSize(4);

    return MySQLPool.pool(vertx, connectOptions, poolOptions);
  }

  public static Pool createPgPool(final BrokerConfig configuration, final Vertx vertx) {
    final var connectOptions = new PgConnectOptions()
      .setHost(configuration.getDbConfig().getHost())
      .setPort(configuration.getDbConfig().getPort())
      .setDatabase(configuration.getDbConfig().getDatabase())
      .setUser(configuration.getDbConfig().getUser())
      .setPassword(configuration.getDbConfig().getPassword());

    var poolOptions = new PoolOptions()
      .setMaxSize(4);

    return PgPool.pool(vertx, connectOptions, poolOptions);
  }

}
