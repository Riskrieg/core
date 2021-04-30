package com.riskrieg.core.internal.action;

import javax.annotation.Nullable;

public interface CompletableAction<T> extends Action<T> {

  @Nullable
  T complete();

}
