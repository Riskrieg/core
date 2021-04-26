package com.riskrieg.core.internal.action.api;

import java.util.function.Consumer;
import javax.annotation.Nullable;

public interface ApiAction<T> {

  void complete(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

  default void complete(@Nullable Consumer<? super T> success) {
    this.complete(success, null);
  }

  default void complete() {
    this.complete(null);
  }

}
