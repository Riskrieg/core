package com.riskrieg.core.internal;

import java.time.Instant;
import java.util.Objects;

public final class Moment {

  private final long epochSecond;
  private final int nanos;

  private Moment(long epochSecond, int nanos) {
    this.epochSecond = epochSecond;
    this.nanos = nanos;
  }

  private Moment(Instant instant) {
    this.epochSecond = instant.getEpochSecond();
    this.nanos = instant.getNano();
  }

  public static Moment of(Instant instant) {
    return new Moment(instant);
  }

  public static Moment now() {
    return new Moment(Instant.now());
  }

  public Instant asInstant() {
    return Instant.ofEpochSecond(epochSecond, nanos);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Moment moment = (Moment) o;
    return epochSecond == moment.epochSecond && nanos == moment.nanos;
  }

  @Override
  public int hashCode() {
    return Objects.hash(epochSecond, nanos);
  }

}
