package com.riskrieg.core.api.requests;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public interface GameAction<T> {

  default void queue() {
    queue(null);
  }

  default void queue(@Nullable Consumer<? super T> success) {
    queue(success, null);
  }

  void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

  @NonNull
  Optional<T> complete();

}
