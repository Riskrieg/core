package com.riskrieg.core.internal.legacy;

import com.riskrieg.core.api.game.TimePoint;

public record LegacyTimePoint(long epochSecond, long nanos) {

  public TimePoint toTimePoint() {
    return new TimePoint(epochSecond, nanos);
  }

}
