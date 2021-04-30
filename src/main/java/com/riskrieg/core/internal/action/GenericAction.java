package com.riskrieg.core.internal.action;

import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

  @Nonnull
  @Override
  public Optional<T> complete(@Nullable Consumer<? super Throwable> failure) {
    if (failure != null && throwable != null) {
      failure.accept(throwable);
    }
    return Optional.ofNullable(value);
  }

}
