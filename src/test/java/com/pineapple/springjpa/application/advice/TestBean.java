package com.pineapple.springjpa.application.advice;

public class TestBean {
  private String name;

  public TestBean() {}

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object handle(String name) {
    return null;
  }
}
