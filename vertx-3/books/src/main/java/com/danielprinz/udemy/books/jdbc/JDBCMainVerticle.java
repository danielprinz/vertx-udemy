package com.danielprinz.udemy.books.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danielprinz.udemy.books.Book;
import com.danielprinz.udemy.books.InMemoryBookStore;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class JDBCMainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(JDBCMainVerticle.class);
  private InMemoryBookStore store = new InMemoryBookStore();
  private JDBCBookRepository bookRepository;

  @Override
  public void start(Promise<Void> start) throws Exception {
    LOG.debug("starting...");
    //Initialize Database Connection
    bookRepository = new JDBCBookRepository(vertx);

    //Initialize Web Server Routes
    Router books = Router.router(vertx);
    books.route().handler(BodyHandler.create());
    books.route("/*").handler(StaticHandler.create());

    //GET /books
    getAll(books);
    //GET /books/:isbn -> fetch one book
    getBookByISBN(books);
    //POST /books
    createBook(books);
    //PUT /books/:isbn
    updateBook(books);
    //DELETE /books/:isbn -> delete one book from in memory store
    deleteBook(books);

    registerErrorHandler(books);

    vertx.createHttpServer().requestHandler(books).listen(8888, http -> {
      if (http.succeeded()) {
        start.complete();
        LOG.info("HTTP server started on port 8888");
      } else {
        start.fail(http.cause());
      }
    });
  }

  private void deleteBook(final Router books) {
    books.delete("/books/:isbn").handler(req -> {
      final String isbn = req.pathParam("isbn");
      bookRepository.delete(isbn).onComplete(ar -> {
        if (ar.failed()){
          //Forward error
          req.fail(ar.cause());
          return;
        }
        // Return response
        if (ar.result() == null) {
          //Book not found
          req.response()
            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
            .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
            .end(new JsonObject().put("error", "Book not found!").encode());
        } else {
          //Deleted a book
          req.response()
            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
            .end(new JsonObject().put("isbn", ar.result()).encode());
        }
      });
    });
  }

  private void getBookByISBN(final Router books) {
    books.get("/books/:isbn").handler(req -> {
      final String isbn = req.pathParam("isbn");
      req.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .end(JsonObject.mapFrom(store.get(isbn)).encode());
    });
  }

  private void registerErrorHandler(final Router books) {
    books.errorHandler(500, event -> {
      LOG.error("Failed: ", event.failure());
      if (event.failure() instanceof  IllegalArgumentException) {
        event.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
          .end(new JsonObject().put("error", event.failure().getMessage()).encode());
        return;
      }
      event.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        .end(new JsonObject().put("error", event.failure().getMessage()).encode());
    });
  }

  private void updateBook(final Router books) {
    books.put("/books/:isbn").handler(req -> {
      final String isbn = req.pathParam("isbn");
      final JsonObject requestBody = req.getBodyAsJson();
      final Book updatedBook = store.update(isbn, requestBody.mapTo(Book.class));
      //Return response
      req.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .end(JsonObject.mapFrom(updatedBook).encode());
    });
  }

  private void createBook(final Router books) {
    books.post("/books").handler(req -> {
      // Read body
      final JsonObject requestBody = req.getBodyAsJson();
      System.out.println("Request Body: " + requestBody);
      // Store
      bookRepository.add(requestBody.mapTo(Book.class)).onComplete(ar -> {
        if (ar.failed()){
          //Forward error
          req.fail(ar.cause());
          return;
        }
        // Return response
        req.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .setStatusCode(HttpResponseStatus.CREATED.code())
          .end(requestBody.encode());
      });
    });
  }

  private void getAll(final Router books) {
    books.get("/books").handler(req -> {
      bookRepository.getAll().onComplete(ar -> {
        if (ar.failed()){
          //Forward error
          req.fail(ar.cause());
          return;
        }
        // Return response
        req.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(ar.result().encode());
      });
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new JDBCMainVerticle());
  }
}
