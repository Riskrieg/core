package com.riskrieg.core.internal.action;

import java.util.function.Consumer;
import javax.annotation.Nullable;

public interface Action<T> {

  void submit(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

  default void submit(@Nullable Consumer<? super T> success) {
    this.submit(success, null);
  }

  default void submit() {
    this.submit(null);
  }

}