package com.riskrieg.core.api;

import com.riskrieg.core.internal.action.CompletableAction;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface Riskrieg {

  @Nonnull
  @CheckReturnValue
  CompletableAction<Group> createGroup(@Nonnull String id);

  @Nonnull
  @CheckReturnValue
  CompletableAction<Group> retrieveGroupById(String id);

}
