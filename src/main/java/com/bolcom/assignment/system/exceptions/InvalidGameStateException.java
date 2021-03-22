package com.bolcom.assignment.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown if a finished game is loaded.
 * 
 * InvalidGameStateException
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidGameStateException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidGameStateException(String message) {
    super(message);
  }

}
