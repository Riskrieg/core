package com.riskrieg.core.internal.action;

import java.util.function.Consumer;
import javax.annotation.Nullable;

public interface CompletableAction<T> extends Action<T> {

  @Nullable
  T complete(@Nullable Consumer<? super Throwable> failure);

  default T complete() {
    return complete(null);
  }

}
