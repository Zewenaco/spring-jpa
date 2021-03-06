package com.pineapple.springjpa.application.exceptions;

import com.pineapple.springjpa.application.response.GenericError;
import org.springframework.http.HttpStatus;

/** This exception you can use it at any layer except service layer */
public class InvalidInputException extends GenericRuntimeException {

  private static final String ERR_CODE = "ERR.REQUEST.INVALID";

  public InvalidInputException(String message) {
    super(GenericError.builder().code(ERR_CODE).message(message).build(), HttpStatus.BAD_REQUEST);
  }

  public InvalidInputException(String message, String field) {
    super(
        GenericError.builder().code(ERR_CODE).message(message).field(field).build(),
        HttpStatus.BAD_REQUEST);
  }

  public InvalidInputException(GenericError genericError) {
    super(genericError, HttpStatus.BAD_REQUEST);
  }
}
