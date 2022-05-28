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

package com.riskrieg.core.api.game;

import com.riskrieg.core.api.color.ColorPalette;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.identifier.GameIdentifier;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public record Save(GameIdentifier identifier, GameConstants constants, ColorPalette colors,
                   Instant creationTime, Instant updatedTime, GamePhase phase, String mapCodename,
                   Collection<Player> players, Set<Nation> nations, Set<Claim> claims,
                   Class<? extends Game> type) {

  public Save(Game game, Class<? extends Game> type) {
    this(game.identifier(), game.constants(), game.colorPalette(),
        game.creationTime(), game.updatedTime(), game.phase(),
        game.map() == null ? "" : Objects.requireNonNull(game.map()).codename(),
        game.players(), game.nations(), game.claims(),
        type);
  }

  public static final String FILE_EXT = ".json";

}
