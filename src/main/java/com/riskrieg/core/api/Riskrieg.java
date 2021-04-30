package com.riskrieg.core.api;

import com.riskrieg.core.internal.action.Action;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface Riskrieg {

  @Nonnull
  @CheckReturnValue
  Action<Group> createGroup(@Nonnull String id);

  @Nonnull
  @CheckReturnValue
  Action<Group> retrieveGroupById(String id);

}
