package com.danielprinz.udemy.broker.quotes;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;
import lombok.Data;

@Data
public class QuoteEntity {

  String asset;
  BigDecimal bid;
  BigDecimal ask;
  @JsonProperty("last_price")
  BigDecimal lastPrice;
  BigDecimal volume;

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }

}
