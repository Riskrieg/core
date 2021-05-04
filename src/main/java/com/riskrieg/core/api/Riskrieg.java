package com.riskrieg.core.api;

import com.riskrieg.core.internal.action.CompletableAction;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * This is the entry point for the Riskrieg API. You use it to create groups, which are containers for game objects.
 */
public interface Riskrieg {

  /**
   * Creates a new group and returns it. If the group already exists, it simply returns that group.
   *
   * @param id The unique group identifier.
   * @return The group that was created, wrapped in a {@code CompletableAction} object.
   */
  @Nonnull
  @CheckReturnValue
  CompletableAction<Group> createGroup(@Nonnull String id);

  /**
   * Attempts to retrieve an existing group that matches the given identifier.
   *
   * @param id The unique group identifier.
   * @return A CompletableAction object that contains the group or, if the group does not exist or could be retrieved, contains a {@code Throwable} detailing the issue.
   */
  @Nonnull
  @CheckReturnValue
  CompletableAction<Group> retrieveGroupById(@Nonnull String id);

}
