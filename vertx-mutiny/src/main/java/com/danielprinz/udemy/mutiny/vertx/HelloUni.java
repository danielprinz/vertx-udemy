package com.danielprinz.udemy.mutiny.vertx;

import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloUni {

  private static final Logger LOG = LoggerFactory.getLogger(HelloUni.class);

  public static void main(String[] args) {
    // Uni represents a stream that can only emit either an item or a failure event.
    Uni.createFrom().item("Hello")
      .onItem().transform(item -> item + " Mutiny!")
      .onItem().transform(String::toUpperCase)
      .subscribe().with(
        item -> LOG.info("Item: {}", item)
      );

    Uni.createFrom().item("Ignored due to failure")
      .onItem().castTo(Integer.class)
      .subscribe().with(
        item -> LOG.info("Item: {}", item),
        failure -> LOG.error("Failed with: ", failure)
      )
    ;
  }

}
