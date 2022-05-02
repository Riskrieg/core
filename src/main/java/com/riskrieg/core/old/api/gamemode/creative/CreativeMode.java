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

package com.riskrieg.core.old.api.gamemode.creative;

import com.riskrieg.core.old.api.Save;
import com.riskrieg.core.old.api.gamemode.GameID;
import com.riskrieg.core.old.api.gamemode.GameMode;
import com.riskrieg.core.old.api.gamemode.GameState;
import com.riskrieg.core.old.api.map.GameMap;
import com.riskrieg.core.old.api.map.MapOptions;
import com.riskrieg.core.old.api.nation.Nation;
import com.riskrieg.core.old.api.order.TurnOrder;
import com.riskrieg.core.old.api.player.Identity;
import com.riskrieg.core.old.api.player.Player;
import com.riskrieg.core.old.constant.color.ColorId;
import com.riskrieg.core.old.internal.action.Action;
import com.riskrieg.core.old.internal.bundle.ClaimBundle;
import com.riskrieg.core.old.internal.bundle.CurrentStateBundle;
import com.riskrieg.core.old.internal.bundle.LeaveBundle;
import com.riskrieg.core.old.internal.bundle.SkipBundle;
import com.riskrieg.core.old.internal.bundle.UpdateBundle;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;
import java.util.Collection;

public final class CreativeMode implements GameMode { // TODO: Implement

  public CreativeMode() {

  }

  public CreativeMode(Save save) {

  }

  @NonNull
  @Override
  public String displayName() {
    return "Creative";
  }

  @NonNull
  @Override
  public GameID id() {
    return null;
  }

  @NonNull
  @Override
  public Instant creationTime() {
    return null;
  }

  @NonNull
  @Override
  public Instant lastUpdated() {
    return null;
  }

  @NonNull
  @Override
  public GameState gameState() {
    return null;
  }

  @Override
  public void setGameState(@NonNull GameState gameState) {

  }

  @Override
  public boolean isEnded() {
    return false;
  }

  @NonNull
  @Override
  public Collection<Player> players() {
    return null;
  }

  @NonNull
  @Override
  public Collection<Nation> nations() {
    return null;
  }

  @NonNull
  @Override
  public GameMap map() {
    return null;
  }

  @NonNull
  @Override
  public Action<Player> join(@NonNull Identity identity, @NonNull String name, @NonNull ColorId colorId) {
    return null;
  }

  @NonNull
  @Override
  public Action<LeaveBundle> leave(@NonNull Identity identity) {
    return null;
  }

  @NonNull
  @Override
  public Action<GameMap> selectMap(@NonNull RkmMap rkmMap, @NonNull MapOptions options) {
    return null;
  }

  @NonNull
  @Override
  public Action<Nation> selectTerritory(@NonNull Identity identity, @NonNull TerritoryId territoryId) {
    return null;
  }

  @NonNull
  @Override
  public Action<Player> start(@NonNull TurnOrder order) {
    return null;
  }

  @NonNull
  @Override
  public Action<SkipBundle> skip(Identity identity) {
    return null;
  }

  @NonNull
  @Override
  public Action<SkipBundle> skipSelf(Identity identity) {
    return null;
  }

  @NonNull
  @Override
  public Action<ClaimBundle> claim(Identity identity, TerritoryId... territoryIds) {
    return null;
  }

  @NonNull
  @Override
  public Action<UpdateBundle> update() {
    return null;
  }

  @NonNull
  @Override
  public CurrentStateBundle currentTurn() {
    return null; // TODO: Change this
  }

}
