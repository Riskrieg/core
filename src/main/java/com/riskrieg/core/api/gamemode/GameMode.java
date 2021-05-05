package com.riskrieg.core.api.gamemode;

import com.riskrieg.core.api.nation.ClaimResult;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.order.TurnOrder;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.map.GameMap;
import com.riskrieg.core.unsorted.map.MapOptions;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import java.awt.Color;
import java.time.Instant;
import java.util.Collection;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface GameMode {

  @Nonnull
  String displayName();

  @Nonnull
  GameID id();

  @Nonnull
  Instant creationTime();

  @Nonnull
  Instant lastUpdated();

  @Nonnull
  GameState gameState();

  void setGameState(@Nonnull GameState gameState);

  boolean isEnded();

  @Nonnull
  Collection<Player> players();

  @Nonnull
  Collection<Nation> nations();

  @Nonnull
  GameMap map();

  @Nonnull
  @CheckReturnValue
  Action<Player> join(@Nonnull Identity identity, @Nonnull String name, @Nonnull Color color);

  @Nonnull
  @CheckReturnValue
  default Action<Player> join(@Nonnull String name, @Nonnull Color color) {
    return this.join(Identity.random(), name, color);
  }

  @Nonnull
  @CheckReturnValue
  Action<Player> leave(@Nonnull Identity identity);

  @Nonnull
  @CheckReturnValue
  Action<GameMap> selectMap(@Nonnull RkmMap rkmMap, @Nonnull MapOptions options);

  @Nonnull
  @CheckReturnValue
  Action<Nation> formNation(@Nonnull Identity identity, @Nonnull TerritoryId territoryId);

  @Nonnull
  @CheckReturnValue
  Action<Player> start(@Nonnull TurnOrder order);

  @Nonnull
  @CheckReturnValue
  Action<ClaimResult> claim(Identity identity, TerritoryId... territoryIds);

  @Nonnull
  @CheckReturnValue
  Action<Player> updateTurn();

}
