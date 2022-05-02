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

package com.riskrieg.core.old.internal.action;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public final class GenericAction<T> implements CompletableAction<T> {

  private final T value;
  private final Throwable throwable;

  public GenericAction(Throwable throwable) {
    this.value = null;
    this.throwable = throwable;
  }

  public GenericAction(T value) {
    this.value = value;
    this.throwable = null;
  }

  @Override
  public void submit(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
    if (success != null && value != null) {
      success.accept(value);
    }
    if (failure != null && throwable != null) {
      failure.accept(throwable);
    }
  }

  @NonNull
  @Override
  public Optional<T> complete(@Nullable Consumer<? super Throwable> failure) {
    if (failure != null && throwable != null) {
      failure.accept(throwable);
    }
    return Optional.ofNullable(value);
  }

}
