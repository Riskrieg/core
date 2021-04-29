package com.riskrieg.core.unsorted.player;

import java.util.Objects;
import java.util.UUID;

public final class Identity {

  private final String id;

  private Identity(String id) {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("Field 'id' of type String cannot be blank");
    }
    this.id = id;
  }

  private Identity() {
    this(UUID.randomUUID().toString());
  }

  public static Identity of(String id) {
    return new Identity(id);
  }

  public static Identity random() {
    return new Identity();
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Identity identity = (Identity) o;
    return id.equals(identity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
