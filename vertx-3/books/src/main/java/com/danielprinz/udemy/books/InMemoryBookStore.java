package com.danielprinz.udemy.books;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class InMemoryBookStore {

  private Map<Long, Book> books = new HashMap<>();

  public InMemoryBookStore(){
    books.put(1L, new Book(1L, "Vert.x in Action"));
    books.put(2L, new Book(2L, "Building Microservices"));
  }

  public JsonArray getAll(){
    JsonArray all = new JsonArray();
    books.values().forEach(book -> {
      all.add(JsonObject.mapFrom(book));
    });
    return all;
  }

  public void add(final Book entry) {
    books.put(entry.getIsbn(), entry);
  }

  public Book update(final String isbn, final Book entry) {
    Long key = Long.parseLong(isbn);
    if (key != entry.getIsbn()) {
      throw new IllegalArgumentException("ISBN does not match!");
    }
    books.put(key, entry);
    return entry;
  }

  public Book get(final String isbn) {
    Long key = Long.parseLong(isbn);
    return books.get(key);
  }

  public Book delete(final String isbn) {
    Long key = Long.parseLong(isbn);
    return books.remove(key);
  }
}
