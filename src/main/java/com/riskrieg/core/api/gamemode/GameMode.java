package com.riskrieg.core.api.gamemode;

import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.map.MapOptions;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.order.TurnOrder;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.color.ColorId;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.action.GenericAction;
import com.riskrieg.core.internal.bundle.ClaimBundle;
import com.riskrieg.core.internal.bundle.CurrentStateBundle;
import com.riskrieg.core.internal.bundle.LeaveBundle;
import com.riskrieg.core.internal.bundle.SkipBundle;
import com.riskrieg.core.internal.bundle.UpdateBundle;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
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
  Action<Player> join(@Nonnull Identity identity, @Nonnull String name, @Nonnull ColorId colorId);

  @Nonnull
  @CheckReturnValue
  default Action<Player> join(@Nonnull String name, @Nonnull ColorId colorId) {
    return this.join(Identity.random(), name, colorId);
  }

  @CheckReturnValue
  default Action<Boolean> changeName(@Nonnull Identity identity, String newName) {
    for (Player p : players()) {
      if (p.identity().equals(identity)) {
        p.setName(newName);
        return new GenericAction<>(true);
      }
    }
    return new GenericAction<>(false);
  }

  @Nonnull
  @CheckReturnValue
  Action<LeaveBundle> leave(@Nonnull Identity identity);

  @Nonnull
  @CheckReturnValue
  default Action<LeaveBundle> leave(@Nonnull ColorId colorId) {
    var optPlayer = players().stream().filter(player -> player.colorId().equals(colorId)).findAny();
    return optPlayer.map(player -> leave(player.identity())).orElseGet(() -> new GenericAction<>(new IllegalStateException("no player with that color is present")));
  }

  @Nonnull
  @CheckReturnValue
  Action<GameMap> selectMap(@Nonnull RkmMap rkmMap, @Nonnull MapOptions options);

  @Nonnull
  @CheckReturnValue
  Action<Nation> selectTerritory(@Nonnull Identity identity, @Nonnull TerritoryId territoryId);

  @Nonnull
  @CheckReturnValue
  Action<Player> start(@Nonnull TurnOrder order);

  @Nonnull
  @CheckReturnValue
  Action<SkipBundle> skip(Identity identity);

  @Nonnull
  @CheckReturnValue
  Action<SkipBundle> skipSelf(Identity identity);

  @Nonnull
  @CheckReturnValue
  Action<ClaimBundle> claim(Identity identity, TerritoryId... territoryIds);

  @Nonnull
  @CheckReturnValue
  Action<UpdateBundle> update();

  @Nonnull
  @CheckReturnValue
  CurrentStateBundle currentTurn();

}
