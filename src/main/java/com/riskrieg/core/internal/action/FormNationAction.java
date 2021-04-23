package com.riskrieg.core.internal.action;

import com.riskrieg.core.map.GameTerritory;
import com.riskrieg.core.map.TerritoryType;
import com.riskrieg.map.territory.TerritoryId;
import com.riskrieg.core.nation.Nation;
import com.riskrieg.core.player.Player;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class FormNationAction implements GameAction<Nation> {

  private final Player player;
  private final TerritoryId id;
  private final Collection<Nation> nations;

  public FormNationAction(Player player, TerritoryId id, Collection<Nation> nations) {
    this.player = player;
    this.id = id;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super Nation> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      Nation nation = new Nation(player.identity(), new GameTerritory(id, TerritoryType.CAPITAL));
      nations.add(nation);
      if (success != null) {
        success.accept(nation);
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
