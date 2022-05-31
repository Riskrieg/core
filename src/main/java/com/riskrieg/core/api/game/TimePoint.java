package com.riskrieg.core.api.game;

import java.time.Instant;

public record TimePoint(long seconds, long nanos) {

  public static TimePoint fromInstant(Instant instant) {
    return new TimePoint(instant.getEpochSecond(), instant.getNano());
  }

  public Instant toInstant() {
    return Instant.ofEpochSecond(seconds, nanos);
  }

}
