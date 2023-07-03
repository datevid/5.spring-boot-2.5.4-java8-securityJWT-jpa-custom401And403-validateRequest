package net.demo.exception;

import org.springframework.http.HttpStatus;

public class ExceptionAuthSecurity extends RuntimeException {

  private final String message;
  private final HttpStatus httpStatus;

  public ExceptionAuthSecurity(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
