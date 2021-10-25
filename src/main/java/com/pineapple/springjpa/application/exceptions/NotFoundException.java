package com.pineapple.springjpa.application.exceptions;

import com.pineapple.springjpa.application.response.GenericError;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GenericRuntimeException {

  private static final String ERR_CODE = "ERR.NOT_FOUND.RESOURCE";

  public NotFoundException(String message) {
    super(GenericError.builder().message(message).code(ERR_CODE).build(), HttpStatus.NOT_FOUND);
  }

  public NotFoundException(GenericError genericError) {
    super(genericError, HttpStatus.NOT_FOUND);
  }
}
