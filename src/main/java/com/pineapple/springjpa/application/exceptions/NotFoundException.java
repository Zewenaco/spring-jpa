package com.pineapple.springjpa.application.exceptions;

import com.pineapple.springjpa.application.response.GenericError;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GenericRuntimeException {

  public NotFoundException(String message) {
    super(GenericError.builder().message(message).build(), HttpStatus.NOT_FOUND);
  }

  public NotFoundException(GenericError genericError) {
    super(genericError, HttpStatus.NOT_FOUND);
  }
}
