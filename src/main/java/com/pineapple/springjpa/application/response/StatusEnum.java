package com.pineapple.springjpa.application.response;

public enum StatusEnum {
  SUCCESS("success"),
  FAIL("fail"),
  ERROR("error");

  protected String status;

  StatusEnum(String status) {
    this.status = status;
  }
}
