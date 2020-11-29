package com.danielprinz.udemy.vertx_starter.json;

public class Person {

  private Integer id;
  private String name;
  private boolean lovesVertx;

  public Person() {
    // Default constructor for Jackson
  }

  public Person(final Integer id, final String name, final boolean lovesVertx) {
    this.id = id;
    this.name = name;
    this.lovesVertx = lovesVertx;
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public boolean isLovesVertx() {
    return lovesVertx;
  }

  public void setLovesVertx(final boolean lovesVertx) {
    this.lovesVertx = lovesVertx;
  }
}
