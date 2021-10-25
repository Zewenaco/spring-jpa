package com.pineapple.springjpa.application.exceptions;

import com.pineapple.springjpa.application.response.GenericError;
import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class GenericRuntimeException extends RuntimeException {

  protected Logger logger = LoggerFactory.getLogger(GenericRuntimeException.class);

  @Getter protected final HttpStatus httpStatus;
  @Getter protected final transient GenericError genericError;
  private static final String DEBUG_MESSAGE_TEMPLATE = "%s";
  private static final String ERR_CODE = "ERR.GENERIC_ERROR";

  public GenericRuntimeException(String message) {
    super(message);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.genericError = GenericError.builder().code(ERR_CODE).message(message).build();
    this.logTraceMessage(this);
  }

  protected GenericRuntimeException(GenericError genericError, HttpStatus httpStatus) {
    super(genericError.getMessage());
    this.httpStatus = httpStatus;
    this.genericError = genericError;
    this.logTraceMessage(this);
  }

  protected String getStackTraceFirstPosition(StackTraceElement[] stackTraceElements) {
    return Arrays.stream(stackTraceElements)
        .findFirst()
        .map(StackTraceElement::toString)
        .orElse(StringUtils.EMPTY);
  }

  protected void logTraceMessage(Throwable throwable) {
    Optional.ofNullable(throwable)
        .ifPresent(
            throwableOpt ->
                logger.debug(
                    String.format(
                        DEBUG_MESSAGE_TEMPLATE,
                        this.getStackTraceFirstPosition(throwableOpt.getStackTrace()))));
  }
}
