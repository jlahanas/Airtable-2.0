package com.speakupcambridge.exceptions;

public class UnexpectedJsonFormatException extends RuntimeException {
  public UnexpectedJsonFormatException(Throwable cause) {
    super(cause);
  }

  public UnexpectedJsonFormatException(String message) {
    super(String.format("Unexpected JSON format: %s", message));
  }

  public UnexpectedJsonFormatException(String message, Throwable cause) {
    super(String.format("Unexpected JSON format: %s", message), cause);
  }
}
