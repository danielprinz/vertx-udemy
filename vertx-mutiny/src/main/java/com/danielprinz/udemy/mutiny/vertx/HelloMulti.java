package com.danielprinz.udemy.mutiny.vertx;

import io.smallrye.mutiny.Multi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class HelloMulti {

  private static final Logger LOG = LoggerFactory.getLogger(HelloMulti.class);

  public static void main(String[] args) {
    // Multi represents a stream of data. A stream can emit 0, 1, n, or an infinite number of items.
    Multi.createFrom().items(IntStream.rangeClosed(0, 10).boxed())
      .onItem().transform(value -> value * 2)
      .onFailure().invoke(failure -> LOG.error("Transformation failed with: ", failure))
      .onItem().transform(String::valueOf)
      .select().first(4)
      .subscribe().with(
        LOG::info,
        failure -> LOG.error("Failed with: ", failure)
      );
  }
}
