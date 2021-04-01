package com.riskrieg.response;

import java.util.Optional;

public class Response {

  private final boolean success;
  private final String message;

  public Response(boolean success) {
    this.success = success;
    this.message = null;
  }

  public Response(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public boolean success() {
    return success;
  }

  public Optional<String> getMessage() {
    return Optional.ofNullable(message);
  }

}
