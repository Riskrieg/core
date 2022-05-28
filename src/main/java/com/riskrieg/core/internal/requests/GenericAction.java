/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
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

package com.riskrieg.core.internal.requests;

import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public record GenericAction<T>(T value, Throwable throwable) implements GameAction<T> {

  public GenericAction {
    if (value == null) {
      Objects.requireNonNull(throwable);
    } else if (throwable == null) {
      Objects.requireNonNull(value);
    }
  }

  public GenericAction(T value) {
    this(value, null);
  }

  public GenericAction(Throwable throwable) {
    this(null, throwable);
  }

  @Override
  public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
    if (success != null && value != null) {
      success.accept(value);
    }
    if (failure != null && throwable != null) {
      failure.accept(throwable);
    }
  }

  @NonNull
  @Override
  public T complete() {
    if (value == null) {
      if (throwable != null) {
        throw new RuntimeException(throwable); // TODO: Hacky but meh, fine for now
      } else {
        throw new NullPointerException("Value passed to GenericAction<T> is null, and complete() cannot return null");
      }
    }
    return value;
  }

}
