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

package com.riskrieg.core.old.api.gamemode;

import com.riskrieg.core.old.api.player.Identity;
import com.riskrieg.core.old.internal.action.Action;
import com.riskrieg.core.old.internal.bundle.AllianceBundle;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;

public interface AlliableMode extends GameMode {

  boolean allied(Identity identity1, Identity identity2);

  @NonNull
  @CheckReturnValue
  Action<AllianceBundle> ally(Identity identity1, Identity identity2);

  @NonNull
  @CheckReturnValue
  Action<AllianceBundle> unally(Identity identity1, Identity identity2);

}
