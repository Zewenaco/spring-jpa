package com.pineapple.springjpa.application.advice;

import Util.UtilityTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pineapple.springjpa.application.exceptions.NotFoundException;
import com.pineapple.springjpa.application.response.GenericResponse;
import com.pineapple.springjpa.application.response.StatusEnum;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.assertj.core.util.Lists;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
public class WebRestControllerAdviceTest {

  private final ObjectMapper mapper = new ObjectMapper();
  @InjectMocks private WebRestControllerAdvice webRestControllerAdvice;
  @Mock private HttpInputMessage httpInputMessage;

  @BeforeEach
  void setup() {
    this.webRestControllerAdvice = new WebRestControllerAdvice(mapper);
  }

  @Test
  void testShouldHandleSuccessfullyHttpMessageNotReadableException() {
    String msg = "Error";
    ResponseEntity<GenericResponse> responseEntity =
        webRestControllerAdvice.handleValidationExceptions(
            new HttpMessageNotReadableException(msg, httpInputMessage));
    UtilityTest.assertAttrOfObject(responseEntity.getBody(), Lists.emptyList());
    UtilityTest.assertAttrOfObject(responseEntity.getBody().getError(), Lists.emptyList());

    Assertions.assertEquals(
        HttpStatus.BAD_REQUEST,
        responseEntity.getStatusCode(),
        "Status Code must be 400 Bad Request");
    Assertions.assertEquals(
        StatusEnum.FAIL.toString(),
        responseEntity.getBody().getStatus(),
        "Body result status must be 'FAIL'");
    Assertions.assertNotNull(responseEntity.getBody(), "GenericResponse shouldn't be null");
    Assertions.assertNotNull(responseEntity.getBody().getError(), "GenericError shouldn't be null");

    Assertions.assertNotNull(
        responseEntity.getBody().getTimeStamp(), "Timestamp should be always setted");
    Assertions.assertEquals(
        msg, responseEntity.getBody().getError().getMessage(), "Message must be equal to 'Error");
    Assertions.assertEquals(
        msg, responseEntity.getBody().getError().getField(), "Message must be equal to 'Error");
  }

  @Test
  void testShouldReturnResponseEntityWithErrorMessageWhenMethodArgumentNotValidException()
      throws NoSuchMethodException {
    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(new TestBean(), "Object");
    errors.rejectValue("name", "invalid", "Error");
    MethodParameter parameter =
        new MethodParameter(TestBean.class.getMethod("handle", String.class), 0);
    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, errors);

    ResponseEntity<GenericResponse> responseEntity =
        webRestControllerAdvice.handleValidationExceptions(ex);
    UtilityTest.assertAttrOfObject(responseEntity.getBody(), Lists.emptyList());
    UtilityTest.assertAttrOfObject(responseEntity.getBody().getError(), List.of("getField"));
    Assertions.assertEquals(
        StatusEnum.FAIL.toString(),
        responseEntity.getBody().getStatus(),
        "Body result status must be 'FAIL'");
    Assertions.assertEquals(
        HttpStatus.BAD_REQUEST,
        responseEntity.getStatusCode(),
        "Status Code must be 400 Bad Request");
  }

  @Test
  void testShouldReturnResponseEntityWithConstraintValidation() {
    String msgConstraintViolation = "Constraint Violation";
    Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();

    constraintViolations.add(
        ConstraintViolationImpl.forParameterValidation(
            "Error",
            null,
            null,
            msgConstraintViolation,
            null,
            null,
            null,
            null,
            PathImpl.createPathFromString(""),
            null,
            null, // ElementType.PARAMETER
            null));

    ResponseEntity<GenericResponse> responseEntity =
        webRestControllerAdvice.handleValidationException(
            new ConstraintViolationException("Constraint Violation", constraintViolations));
    UtilityTest.assertAttrOfObject(responseEntity.getBody(), Lists.emptyList());
    UtilityTest.assertAttrOfObject(responseEntity.getBody().getError(), List.of("getField"));
    Assertions.assertEquals(
        HttpStatus.BAD_REQUEST,
        responseEntity.getStatusCode(),
        "Status Code must be Bad Request Error");
    Assertions.assertEquals(
        StatusEnum.FAIL.toString(),
        responseEntity.getBody().getStatus(),
        "Body result status must be 'FAIL'");
    Assertions.assertEquals(
        msgConstraintViolation,
        responseEntity.getBody().getError().getMessage(),
        "Status Code must be Bad Request Error");
  }

  @Test
  void testShouldHandleNotFoundExceptionSuccessfully() {
    String errorMsg = "Error";
    ResponseEntity<GenericResponse> responseEntity =
        webRestControllerAdvice.handleNotFoundException(new NotFoundException(errorMsg));
    UtilityTest.assertAttrOfObject(responseEntity.getBody(), Lists.emptyList());
    UtilityTest.assertAttrOfObject(responseEntity.getBody().getError(), List.of("getField"));
    Assertions.assertEquals(
        StatusEnum.FAIL.toString(),
        responseEntity.getBody().getStatus(),
        "Body result status must be 'FAIL'");
    Assertions.assertEquals(
        errorMsg,
        responseEntity.getBody().getError().getMessage(),
        "Status Code must be Bad Request Error");
  }

  @Test
  void testShouldHandleRuntimeExceptionExceptionSuccessfully() {
    String errorMsg = "Error";
    ResponseEntity<GenericResponse> responseEntity =
        webRestControllerAdvice.handleRuntimeException(new RuntimeException(errorMsg));
    UtilityTest.assertAttrOfObject(responseEntity.getBody(), Lists.emptyList());
    UtilityTest.assertAttrOfObject(responseEntity.getBody().getError(), List.of("getField"));
    Assertions.assertEquals(
        StatusEnum.ERROR.toString(),
        responseEntity.getBody().getStatus(),
        "Body result status must be 'ERROR'");
    Assertions.assertEquals(
        errorMsg,
        responseEntity.getBody().getError().getMessage(),
        "Status Code must be Bad Request Error");
  }
}
