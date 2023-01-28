package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Customer {

  @Id
  @GeneratedValue
  private Integer id;
  private Integer code;
  private String name;

  public Customer(Integer code, String name) {
    this();
    this.code = code;
    this.name = name;
  }

  protected Customer() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public Integer getCode() {
    return code;
  }

  public String getName() {
    return name;
  }
}
