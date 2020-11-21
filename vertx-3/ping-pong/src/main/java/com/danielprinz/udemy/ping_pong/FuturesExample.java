package com.danielprinz.udemy.ping_pong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class FuturesExample extends AbstractVerticle {

  @Override
  public void start(final Promise<Void> start) throws Exception {
    System.out.println("1 - Start");
    Promise<Void> whenTimer1Fired = Promise.promise();
    vertx.setTimer(1000, id -> {
      System.out.println("3 - Timer fired");
      whenTimer1Fired.complete();
    });
    Promise<Void> whenTimer2Fired = Promise.promise();
    vertx.setTimer(2000, id -> {
      System.out.println("4 - Second Timer fired");
      whenTimer2Fired.complete();
    });
    CompositeFuture.all(whenTimer1Fired.future(), whenTimer2Fired.future()).setHandler(ar -> {
      System.out.println("5 - Both timers fired");
      start.complete();
    });
    System.out.println("2 - Timer Execution Called");
  }

  @Override
  public void stop(final Promise<Void> stop) throws Exception {
    System.out.println("Stop");
  }

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FuturesExample());
  }
}
