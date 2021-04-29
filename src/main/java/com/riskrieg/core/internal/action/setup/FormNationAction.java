package com.riskrieg.core.internal.action.setup;

import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.map.GameMap;
import com.riskrieg.core.unsorted.map.GameTerritory;
import com.riskrieg.core.unsorted.map.TerritoryType;
import com.riskrieg.core.unsorted.nation.Nation;
import com.riskrieg.core.unsorted.player.Identity;
import com.riskrieg.core.unsorted.player.Player;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class FormNationAction implements Action<Nation> {

  private final Identity identity;
  private final TerritoryId id;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public FormNationAction(Identity identity, TerritoryId id, GameState gameState, GameMap gameMap, Collection<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.id = id;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super Nation> success, @Nullable Consumer<? super Throwable> failure) {
    switch (gameState) {
      case ENDED, RUNNING -> throw new IllegalStateException("Capitals can only be selected during the setup phase");
      case SETUP -> {
        try {
          if (players.stream().noneMatch(p -> p.identity().equals(identity))) {
            throw new IllegalStateException("Player is not present");
          }
          if (!gameMap.isSet()) {
            throw new IllegalStateException("A valid map must be selected before selecting a capital");
          }
          if (gameMap.getGraph().vertexSet().stream().noneMatch(t -> t.id().equals(id))) {
            throw new IllegalStateException("No such territory exists on the selected map");
          }
          var territoryIds = nations.stream().map(Nation::territories).flatMap(Set::stream).collect(Collectors.toSet());
          if (territoryIds.contains(id)) {
            throw new IllegalStateException("That territory is already taken by someone else");
          }
          Nation nation = new Nation(identity, new GameTerritory(id, TerritoryType.CAPITAL));
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
  }

}
