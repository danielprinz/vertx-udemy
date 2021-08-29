package com.danielprinz.udemy.quarkus.vertx;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.core.Vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;

@ApplicationScoped
public class VerticleDeployer {

  public void init(@Observes StartupEvent startupEvent, Vertx vertx, Instance<AbstractVerticle> verticles) {
    verticles.forEach(v ->
      vertx.deployVerticle(v).await().indefinitely()
    );
  }

}
