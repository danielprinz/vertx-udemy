package com.danielprinz.udemy.ping_pong.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * Generic Version of {@link MessageCodec}, which can be used for event bus communication.
 * Make sure to use immutable objects. Sending mutable lists will fail if concurrently accessed.
 * For this purpose use {@link io.vertx.core.json.JsonArray} and {@link io.vertx.core.json.JsonObject}
 * as the built in message codecs support it by copying the list.
 * See: {@link io.vertx.core.eventbus.impl.codecs.JsonArrayMessageCodec}
 *
 * Note: Not working with clustered event bus
 *
 * @param <T> type of the event
 */
public class LocalMessageCodec<T> implements MessageCodec<T, T> {

  private final String typeName;

  public LocalMessageCodec(Class<T> type) {
    this.typeName = type.getName();
  }

  @Override
  public void encodeToWire(final Buffer buffer, final T t) {
    throw new UnsupportedOperationException("Only local encode is supported.");
  }

  @Override
  public T decodeFromWire(final int pos, final Buffer buffer) {
    throw new UnsupportedOperationException("Only local decode is supported.");
  }

  @Override
  public T transform(final T obj) {
    return obj;
  }

  @Override
  public String name() {
    return this.typeName;
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
