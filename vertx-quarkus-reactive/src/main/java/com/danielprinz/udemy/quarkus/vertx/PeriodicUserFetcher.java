package com.danielprinz.udemy.quarkus.vertx;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ApplicationScoped
public class PeriodicUserFetcher extends AbstractVerticle {

  static final String ADDRESS = PeriodicUserFetcher.class.getName();
  private static final Logger LOG = LoggerFactory.getLogger(PeriodicUserFetcher.class);

  @Override
  public Uni<Void> asyncStart() {
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultHost("localhost").setDefaultPort(8080)
    );

    vertx.periodicStream(Duration.ofSeconds(5).toMillis())
      .toMulti()
      .subscribe()
      .with(item -> {
        LOG.info("Fetch all users!");
        client.get("/users").send()
          .subscribe().with(result -> {
            var body = result.bodyAsJsonArray();
            LOG.info("All users from http response: {}", body);
            vertx.eventBus().publish(ADDRESS, body);
          });
      });

    return Uni.createFrom().voidItem();
  }
}
