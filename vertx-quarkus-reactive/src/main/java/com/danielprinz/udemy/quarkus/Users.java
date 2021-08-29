package com.danielprinz.udemy.quarkus;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Cacheable
public class Users extends PanacheEntity {

  @Column(length = 40, unique = true)
  public String name;

}
