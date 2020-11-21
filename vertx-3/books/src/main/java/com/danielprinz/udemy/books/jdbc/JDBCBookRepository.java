package com.danielprinz.udemy.books.jdbc;

import java.util.List;

import com.danielprinz.udemy.books.Book;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;

public class JDBCBookRepository {

  private SQLClient sql;

  public JDBCBookRepository(final Vertx vertx) {
    final JsonObject config = new JsonObject();
    config.put("url", "jdbc:postgresql://127.0.0.1/books");
    config.put("driver_class", "org.postgresql.Driver");
    config.put("user", "postgres");
    config.put("password", "secret"); //For production manage password in config file or through env
    sql = JDBCClient.createShared(vertx, config);
  }

  public Future<JsonArray> getAll() {
    final Promise<JsonArray> getAll = Promise.promise();
    sql.query("SELECT * FROM books", ar -> {
      if (ar.failed()){
        //Forward error
        getAll.fail(ar.cause());
        return;
      }
      //Return result
      final List<JsonObject> rows = ar.result().getRows();
      final JsonArray result = new JsonArray();
      rows.forEach(result::add);
      getAll.complete(result);
    });
    return getAll.future();
  }

  public Future<Void> add(final Book bookToAdd) {
    final Promise<Void> added = Promise.promise();
    final JsonArray params = new JsonArray().add(bookToAdd.getIsbn()).add(bookToAdd.getTitle());
    sql.updateWithParams("INSERT INTO books (isbn, title) VALUES (?, ?)", params, ar -> {
      if (ar.failed()){
        //Forward error
        added.fail(ar.cause());
        return;
      }
      //Return failure if updated count is not 1
      if (ar.result().getUpdated() != 1) {
        added.fail(new IllegalStateException("Wrong update count on insert " + ar.result()));
        return;
      }
      //Return success
      added.complete();
    });
    return added.future();
  }

  public Future<String> delete(final String isbn) {
    final Promise<String> deleted = Promise.promise();
    final JsonArray params = new JsonArray().add(Long.parseLong(isbn));
    sql.updateWithParams("DELETE FROM books where books.isbn = ?", params, ar -> {
      if (ar.failed()){
        //Forward error
        deleted.fail(ar.cause());
        return;
      }
      //Nothing was deleted
      if (ar.result().getUpdated() == 0) {
        deleted.complete();
        return;
      }
      //Return deleted isbn
      deleted.complete(isbn);
    });
    return deleted.future();
  }
}
