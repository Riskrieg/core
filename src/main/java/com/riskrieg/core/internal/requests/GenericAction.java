package com.riskrieg.core.internal.requests;

import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public record GenericAction<T>(T value, Throwable throwable) implements GameAction<T> {

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
  public Optional<T> complete() {
    return Optional.ofNullable(value);
  }

}
