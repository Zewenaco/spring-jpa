package com.pineapple.springjpa.application.constant;

public final class ValidationMessage {
  public static final String NOT_NULL = "Can't be null";
  public static final String NOT_EMPTY = "Can't be empty";
  public static final String CURRENCY_VALIDATION = "Only contains letters - iso alpha 3 code";
  public static final String NUMERIC_GREATER_THAN_ZERO = "The number should be greater than 0";
  public static final String NUMERIC_GREATER_OR_EQUAL_THAN_ZERO =
      "The number should be greater or equal to 0";
}
