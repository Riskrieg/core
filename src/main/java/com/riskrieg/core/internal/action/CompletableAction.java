package com.riskrieg.core.internal.action;

import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CompletableAction<T> extends Action<T> {

  @Nonnull
  Optional<T> complete(@Nullable Consumer<? super Throwable> failure);

  @Nonnull
  default Optional<T> complete() {
    return complete(null);
  }

}
