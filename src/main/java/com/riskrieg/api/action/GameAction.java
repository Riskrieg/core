package com.riskrieg.api.action;

import java.util.function.Consumer;
import javax.annotation.Nullable;

public interface GameAction<T> {

  void submit(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

  default void submit(@Nullable Consumer<? super T> success) {
    this.submit(success, null);
  }

  default void submit() {
    this.submit(null);
  }

}
