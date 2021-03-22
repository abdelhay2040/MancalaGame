package com.bolcom.assignment.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown if a gameId requested is not found.
 * 
 * GameNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public GameNotFoundException(String message) {
    super(message);
  }

}
