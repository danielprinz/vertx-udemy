package com.danielprinz.udemy.vertx_websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

public class WebSocketHandler implements Handler<ServerWebSocket> {

  private static final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);
  public static final String PATH = "/ws/simple/prices";
  private final PriceBroadcast broadcast;

  public WebSocketHandler(final Vertx vertx) {
    this.broadcast = new PriceBroadcast(vertx);
  }

  @Override
  public void handle(final ServerWebSocket ws) {
    if (!PATH.equalsIgnoreCase(ws.path())) {
      LOG.info("Rejected wrong path: {}", ws.path());
      ws.writeFinalTextFrame("Wrong path. Only " + PATH + " is accepted!");
      closeClient(ws);
      return;
    }
    LOG.info("Opening web socket connection: {}, {}", ws.path(), ws.textHandlerID());
    ws.accept();
    ws.frameHandler(frameHandler(ws));
    ws.endHandler(onClose -> {
      LOG.info("Closed {}", ws.textHandlerID());
      broadcast.unregister(ws);
    });
    ws.exceptionHandler(err -> LOG.error("Failed: ", err));
    ws.writeTextMessage("Connected!");
    broadcast.register(ws);
  }

  private Handler<WebSocketFrame> frameHandler(final ServerWebSocket ws) {
    return received -> {
      final String message = received.textData();
      LOG.debug("Received message: {} from client {}", message, ws.textHandlerID());
      if ("disconnect me".equalsIgnoreCase(message)) {
        LOG.info("Client close requested!");
        closeClient(ws);
      } else {
        ws.writeTextMessage("Not supported => (" + message + ")");
      }
    };
  }

  private void closeClient(final ServerWebSocket ws) {
    ws.close((short) 1000, "Normal Closure");
  }
}
