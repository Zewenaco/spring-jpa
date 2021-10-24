package com.pineapple.springjpa.application.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pineapple.springjpa.application.exceptions.GenericRuntimeException;
import com.pineapple.springjpa.application.exceptions.InvalidInputException;
import com.pineapple.springjpa.application.exceptions.NotFoundException;
import com.pineapple.springjpa.application.response.GenericError;
import com.pineapple.springjpa.application.response.GenericResponse;
import com.pineapple.springjpa.application.response.StatusEnum;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebRestControllerAdvice {

  protected Logger logger = LoggerFactory.getLogger(WebRestControllerAdvice.class);
  private final ObjectMapper mapper;

  public WebRestControllerAdvice(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<GenericResponse> handleRuntimeException(RuntimeException runtimeException) {
    Throwable cause = Optional.ofNullable(runtimeException.getCause()).orElse(runtimeException);
    GenericRuntimeException genericRuntimeException =
        this.determinateExceptionByThrowableCause(cause);
    return new ResponseEntity<>(
        GenericResponse.builder()
            .status(StatusEnum.ERROR.toString())
            .error(genericRuntimeException.getGenericError())
            .build(),
        genericRuntimeException.getHttpStatus());
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<GenericResponse> handleNotFoundException(
      NotFoundException notFoundException) {
    Throwable cause = Optional.ofNullable(notFoundException.getCause()).orElse(notFoundException);
    GenericRuntimeException genericRuntimeException =
        this.determinateExceptionByThrowableCause(cause);
    return new ResponseEntity<>(
        GenericResponse.builder()
            .status(StatusEnum.FAIL.toString())
            .error(genericRuntimeException.getGenericError())
            .build(),
        genericRuntimeException.getHttpStatus());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<GenericResponse> handleValidationExceptions(
      HttpMessageNotReadableException ex) {

    InvalidInputException invalidInputException = this.getInvalidInputFromMessageNotReadable(ex);
    return new ResponseEntity<>(
        GenericResponse.builder()
            .status(StatusEnum.ERROR.toString())
            .error(invalidInputException.getGenericError())
            .build(),
        invalidInputException.getHttpStatus());
  }

  /**
   * Triggered when controller expects some @Valid in parameters values from headers/body/url
   *
   * @param ex
   * @return
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<GenericResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    InvalidInputException invalidInputException = this.getErrorFromValidationEx(ex);
    return new ResponseEntity<>(
        GenericResponse.builder()
            .status(StatusEnum.ERROR.toString())
            .error(invalidInputException.getGenericError())
            .build(),
        invalidInputException.getHttpStatus());
  }

  /**
   * Combination from @Valid/@Validated
   *
   * @param constraintViolationException
   * @return
   */
  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<GenericResponse> handleValidationException(
      ConstraintViolationException constraintViolationException) {
    GenericRuntimeException genericRuntimeException =
        this.getGenericErrorFromViolationException(constraintViolationException);
    return new ResponseEntity<>(
        GenericResponse.builder()
            .status(StatusEnum.ERROR.toString())
            .error(genericRuntimeException.getGenericError())
            .build(),
        genericRuntimeException.getHttpStatus());
  }

  public <T extends RuntimeException> T determinateExceptionByThrowableCause(Throwable cause) {
    GenericRuntimeException genericRuntimeException =
        new GenericRuntimeException(cause.getMessage());

    if (GenericRuntimeException.class.isAssignableFrom(cause.getClass())) {
      genericRuntimeException = (GenericRuntimeException) cause;
    }

    return (T) genericRuntimeException;
  }

  public InvalidInputException getInvalidInputFromMessageNotReadable(
      HttpMessageNotReadableException ex) {
    String message = ex.getMessage();
    String field = ex.getLocalizedMessage();
    if (InvalidFormatException.class.isAssignableFrom(ex.getCause().getClass())) {
      InvalidFormatException invalidEx = (InvalidFormatException) ex.getCause();
      message = invalidEx.getOriginalMessage();
      field = invalidEx.getPathReference();
    }
    return new InvalidInputException(message, field);
  }

  public InvalidInputException getErrorFromValidationEx(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    List<GenericError> list = new ArrayList<>();
    Optional.ofNullable(methodArgumentNotValidException)
        .ifPresent(
            ex ->
                list.addAll(
                    ex.getBindingResult().getAllErrors().stream()
                        .map(this::convertObjectErrorToGenericError)
                        .collect(Collectors.toList())));

    String debugMessage;

    try {
      debugMessage = mapper.writeValueAsString(list);
    } catch (JsonProcessingException ex) {
      debugMessage = "Error parsing validation message details";
      logger.warn(debugMessage, ex);
    }
    return new InvalidInputException(debugMessage);
  }

  protected GenericError convertObjectErrorToGenericError(ObjectError objectError) {

    GenericError genericError = new GenericError();
    String fieldName;
    if (FieldError.class.isAssignableFrom(objectError.getClass())) {
      fieldName = ((FieldError) objectError).getField();
    } else {
      fieldName = objectError.getObjectName();
    }
    genericError.setField(fieldName);
    genericError.setMessage(objectError.getDefaultMessage());
    return genericError;
  }

  public InvalidInputException getGenericErrorFromViolationException(
      ConstraintViolationException constraintViolationException) {

    GenericError genericError =
        this.buildGenericErrorFromConstraintViolation(
            constraintViolationException.getConstraintViolations(), GenericError.class);

    return new InvalidInputException(genericError);
  }

  public <T extends GenericError> T buildGenericErrorFromConstraintViolation(
      Set<ConstraintViolation<?>> constraintViolations, Class<? extends GenericError> cl) {

    GenericError genericError;
    String messages;
    try {
      genericError = ConstructorUtils.invokeConstructor(cl);
      messages =
          constraintViolations.stream()
              .map(ConstraintViolation::getMessage)
              .filter(Objects::nonNull)
              .collect(Collectors.joining());
    } catch (NoSuchMethodException
        | InvocationTargetException
        | IllegalAccessException
        | InstantiationException e) {
      logger.warn("There is an error processing buildGenericErrorFromConstraintViolation", e);
      throw new GenericRuntimeException(e.getMessage());
    }
    genericError.setMessage(messages);
    return (T) genericError;
  }
}
