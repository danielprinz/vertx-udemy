package com.danielprinz.udemy.books;

public class Book {

  private long isbn;
  private String title;

  public Book(){
    //default constructor
  }

  public Book(final long isbn, final String title) {
    this.isbn = isbn;
    this.title = title;
  }

  public long getIsbn() {
    return isbn;
  }

  public void setIsbn(final long isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }
}
