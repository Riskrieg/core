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

package com.riskrieg.core.api;

import com.riskrieg.core.api.gamemode.GameID;
import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.GameModeType;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.gamemode.brawl.BrawlMode;
import com.riskrieg.core.api.gamemode.classic.ClassicMode;
import com.riskrieg.core.api.gamemode.conquest.ConquestMode;
import com.riskrieg.core.api.gamemode.creative.CreativeMode;
import com.riskrieg.core.api.gamemode.regicide.RegicideMode;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.Moment;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public final class Save {

  private final GameModeType gameType;
  private final GameID id;
  private final Moment creationTime;
  private final Moment lastUpdated;

  private final GameState gameState;
  private final String mapSimpleName;

  private final Deque<Player> players;
  private final Set<Nation> nations;

  public <T extends GameMode> Save(T game) {
    this.id = game.id();
    this.creationTime = Moment.of(game.creationTime());
    this.lastUpdated = Moment.of(game.lastUpdated());
    this.gameState = game.gameState();
    if (game.map().isSet()) {
      this.mapSimpleName = game.map().mapName().simpleName();
    } else {
      this.mapSimpleName = null;
    }
    this.players = new ArrayDeque<>(game.players());
    this.nations = new HashSet<>(game.nations());
    if (game instanceof ClassicMode) {
      this.gameType = GameModeType.CLASSIC;
    } else if (game instanceof ConquestMode) {
      this.gameType = GameModeType.CONQUEST;
    } else if (game instanceof RegicideMode) {
      this.gameType = GameModeType.REGICIDE;
    } else if (game instanceof BrawlMode) {
      this.gameType = GameModeType.BRAWL;
    } else if (game instanceof CreativeMode) {
      this.gameType = GameModeType.CREATIVE;
    } else {
      throw new IllegalArgumentException("provided game mode is not supported");
    }
  }

  public GameModeType gameType() {
    return gameType;
  }

  public GameID id() {
    return id;
  }

  public Moment creationTime() {
    return creationTime;
  }

  public Moment lastUpdated() {
    return lastUpdated;
  }

  public GameState gameState() {
    return gameState;
  }

  public String mapSimpleName() {
    return mapSimpleName;
  }

  public Deque<Player> players() {
    return players;
  }

  public Set<Nation> nations() {
    return nations;
  }

}
