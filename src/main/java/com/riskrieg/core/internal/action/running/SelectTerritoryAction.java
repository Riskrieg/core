package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.map.GameMap;
import com.riskrieg.core.unsorted.map.GameTerritory;
import com.riskrieg.core.unsorted.map.TerritoryType;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class SelectTerritoryAction implements Action<Nation> {

  private final Identity identity;
  private final TerritoryId id;
  private final TerritoryType territoryType;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public SelectTerritoryAction(Identity identity, TerritoryId id, TerritoryType territoryType, GameState gameState, GameMap gameMap,
      Collection<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.id = id;
    this.territoryType = territoryType;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super Nation> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> throw new IllegalStateException("Capitals can only be selected during the setup phase");
        case SETUP -> {
          if (players.stream().noneMatch(p -> p.identity().equals(identity))) {
            throw new IllegalStateException("Player is not present");
          }
          if (!gameMap.isSet()) {
            throw new IllegalStateException("A valid map must be selected before selecting a capital");
          }
          if (gameMap.graph().vertexSet().stream().noneMatch(t -> t.id().equals(id))) {
            throw new IllegalStateException("No such territory exists on the selected map");
          }
          if (nations.stream().anyMatch(n -> n.identity().equals(identity))) {
            throw new IllegalStateException("That player has already selected a capital");
          }
          var territoryIds = nations.stream().map(Nation::territories).flatMap(Set::stream).collect(Collectors.toSet());
          if (territoryIds.contains(id)) {
            throw new IllegalStateException("That territory is already taken by someone else");
          }
          Nation nation = new Nation(identity, new GameTerritory(id, territoryType));
          nations.add(nation);
          if (success != null) {
            success.accept(nation);
          }
        }
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
