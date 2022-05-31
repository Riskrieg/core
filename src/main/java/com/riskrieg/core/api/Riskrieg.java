/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
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

package com.riskrieg.core.api;

import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;

/**
 * The entry point for the Riskrieg API. Represents a repository. Use it to create {@link Group}s, which are containers for {@link Game} objects. {@link Group}s are comparable to
 * folders.
 */
public interface Riskrieg {

  // Constants
  public static final String NAME = "Riskrieg";
  public static final String VERSION = "3.0.0-alpha";

  /**
   * Creates a new {@link Group} and returns it. If a {@link Group} with the provided {@link GroupIdentifier} already exists, it returns that {@link Group}.
   *
   * @param identifier The unique {@link GroupIdentifier} for the {@link Group}.
   * @return {@link GameAction} containing the {@link Group} that was created, or the {@link Group} retrieved if a {@link Group} with the provided {@link GroupIdentifier} already
   * exists.
   */
  @NonNull
  @CheckReturnValue
  GameAction<Group> createGroup(GroupIdentifier identifier);

  /**
   * Attempts to retrieve an existing {@link Group} that matches the given {@link GroupIdentifier}.
   *
   * @param identifier The unique {@link GroupIdentifier} for the {@link Group}.
   * @return {@link GameAction} containing the {@link Group} that was retrieved, or a {@link Throwable} if no {@link Group} could be retrieved.
   */
  @NonNull
  @CheckReturnValue
  GameAction<Group> retrieveGroup(GroupIdentifier identifier);

  /**
   * Attempts to retrieve all {@link Group}s in the given repository.
   *
   * @return {@link GameAction} containing a {@link Collection} of all {@link Group}s in the given repository, or an empty {@link Collection} if there are no {@link Group}s.
   */
  @NonNull
  @CheckReturnValue
  GameAction<Collection<Group>> retrieveAllGroups();

  /**
   * Deletes the {@link Group} with the given {@link GroupIdentifier}. This will also delete all {@link Game}s within that {@link Group}.
   *
   * @param identifier The unique {@link GroupIdentifier} for the {@link Group}.
   * @return {@link GameAction} containing a boolean: {@code true} if the {@link Group} was deleted, or {@code false} if the {@link Group} could not be deleted or already doesn't
   * exist.
   */
  @NonNull
  @CheckReturnValue
  GameAction<Boolean> deleteGroup(GroupIdentifier identifier);

}
