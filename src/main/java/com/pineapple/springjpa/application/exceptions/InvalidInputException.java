package com.pineapple.springjpa.application.exceptions;

import com.pineapple.springjpa.application.response.GenericError;
import org.springframework.http.HttpStatus;

/** This exception you can use it at any layer except service layer */
public class InvalidInputException extends GenericRuntimeException {

  public InvalidInputException(String message) {
    super(GenericError.builder().message(message).build(), HttpStatus.BAD_REQUEST);
  }

  public InvalidInputException(String message, String field) {
    super(GenericError.builder().message(message).field(field).build(), HttpStatus.BAD_REQUEST);
  }

  public InvalidInputException(GenericError genericError) {
    super(genericError, HttpStatus.BAD_REQUEST);
  }
}
