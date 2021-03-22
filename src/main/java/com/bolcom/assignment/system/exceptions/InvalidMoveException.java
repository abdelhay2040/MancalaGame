package com.bolcom.assignment.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * InvalidMoveException
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidMoveException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidMoveException(String message) {
    super(message);
  }

}
