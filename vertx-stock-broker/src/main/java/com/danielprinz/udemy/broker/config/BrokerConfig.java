package com.danielprinz.udemy.broker.config;

import java.util.Objects;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class BrokerConfig {

  int serverPort;
  String version;

  public static BrokerConfig from(final JsonObject config) {
    final Integer serverPort = config.getInteger(ConfigLoader.SERVER_PORT);
    if (Objects.isNull(serverPort)) {
      throw new RuntimeException(ConfigLoader.SERVER_PORT + " not configured!");
    }
    final String version = config.getString("version");
    if (Objects.isNull(version)) {
      throw new RuntimeException("version is not configured in config file!");
    }
    return BrokerConfig.builder()
      .serverPort(serverPort)
      .version(version)
      .build();
  }
}
