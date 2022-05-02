/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021  Aaron Yoder <aaronjyoder@gmail.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.old.internal;

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
