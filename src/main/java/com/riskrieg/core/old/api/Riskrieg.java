/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021  Aaron Yoder <aaronjyoder@gmail.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.old.api;

import com.riskrieg.core.old.internal.action.CompletableAction;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;

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
  @NonNull
  @CheckReturnValue
  CompletableAction<Group> createGroup(@NonNull String id);

  /**
   * Attempts to retrieve an existing group that matches the given identifier.
   *
   * @param id The unique group identifier.
   * @return A CompletableAction object that contains the group or, if the group does not exist or could be retrieved, contains a {@code Throwable} detailing the issue.
   */
  @NonNull
  @CheckReturnValue
  CompletableAction<Group> retrieveGroupById(@NonNull String id);

}
