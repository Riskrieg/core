/**
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

package com.riskrieg.core.api.gamemode.creative;

import com.riskrieg.core.api.Save;
import com.riskrieg.core.api.gamemode.GameID;
import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.map.MapOptions;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.order.TurnOrder;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.color.ColorId;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.ClaimBundle;
import com.riskrieg.core.internal.bundle.CurrentStateBundle;
import com.riskrieg.core.internal.bundle.LeaveBundle;
import com.riskrieg.core.internal.bundle.SkipBundle;
import com.riskrieg.core.internal.bundle.UpdateBundle;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import java.time.Instant;
import java.util.Collection;
import javax.annotation.Nonnull;

public final class CreativeMode implements GameMode { // TODO: Implement

  public CreativeMode() {

  }

  public CreativeMode(Save save) {

  }

  @Nonnull
  @Override
  public String displayName() {
    return "Creative";
  }

  @Nonnull
  @Override
  public GameID id() {
    return null;
  }

  @Nonnull
  @Override
  public Instant creationTime() {
    return null;
  }

  @Nonnull
  @Override
  public Instant lastUpdated() {
    return null;
  }

  @Nonnull
  @Override
  public GameState gameState() {
    return null;
  }

  @Override
  public void setGameState(@Nonnull GameState gameState) {

  }

  @Override
  public boolean isEnded() {
    return false;
  }

  @Nonnull
  @Override
  public Collection<Player> players() {
    return null;
  }

  @Nonnull
  @Override
  public Collection<Nation> nations() {
    return null;
  }

  @Nonnull
  @Override
  public GameMap map() {
    return null;
  }

  @Nonnull
  @Override
  public Action<Player> join(@Nonnull Identity identity, @Nonnull String name, @Nonnull ColorId colorId) {
    return null;
  }

  @Nonnull
  @Override
  public Action<LeaveBundle> leave(@Nonnull Identity identity) {
    return null;
  }

  @Nonnull
  @Override
  public Action<GameMap> selectMap(@Nonnull RkmMap rkmMap, @Nonnull MapOptions options) {
    return null;
  }

  @Nonnull
  @Override
  public Action<Nation> selectTerritory(@Nonnull Identity identity, @Nonnull TerritoryId territoryId) {
    return null;
  }

  @Nonnull
  @Override
  public Action<Player> start(@Nonnull TurnOrder order) {
    return null;
  }

  @Nonnull
  @Override
  public Action<SkipBundle> skip(Identity identity) {
    return null;
  }

  @Nonnull
  @Override
  public Action<SkipBundle> skipSelf(Identity identity) {
    return null;
  }

  @Nonnull
  @Override
  public Action<ClaimBundle> claim(Identity identity, TerritoryId... territoryIds) {
    return null;
  }

  @Nonnull
  @Override
  public Action<UpdateBundle> update() {
    return null;
  }

  @Nonnull
  @Override
  public CurrentStateBundle currentTurn() {
    return null; // TODO: Change this
  }

}
