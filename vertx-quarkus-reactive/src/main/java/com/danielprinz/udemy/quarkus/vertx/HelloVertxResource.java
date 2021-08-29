package com.danielprinz.udemy.quarkus.vertx;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/vertx")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class HelloVertxResource {

  private final WebClient client;

  @Inject
  public HelloVertxResource(Vertx vertx) {
    this.client = WebClient.create(vertx,
      new WebClientOptions().setDefaultHost("localhost").setDefaultPort(8080)
    );
  }

  @GET
  public Uni<JsonArray> get() {
    final var item = new JsonArray();
    item.add(new JsonObject().put("id", 1));
    item.add(new JsonObject().put("id", 2));
    return Uni.createFrom().item(item);
  }

  @GET
  @Path("/users")
  public Uni<JsonArray> getFromUsers() {
    return client.get("/users").send()
      .onItem()
      .transform(HttpResponse::bodyAsJsonArray);
  }

}
