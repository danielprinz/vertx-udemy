package com.danielprinz.udemy.ping_pong.codec;

public class Ping {
  private String message;

  public Ping() {
  }

  public Ping(final String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
