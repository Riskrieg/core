package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.map.GameTerritory;
import com.riskrieg.core.api.map.TerritoryType;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Collection;
import java.util.Deque;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public final class SelectTerritoryAction implements Action<Nation> { // Meant for Brawl Mode only

  private final Identity identity;
  private final TerritoryId id;
  private final TerritoryType territoryType;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Deque<Player> players;
  private final Collection<Nation> nations;

  public SelectTerritoryAction(Identity identity, TerritoryId id, TerritoryType territoryType, GameState gameState, GameMap gameMap,
      Deque<Player> players, Collection<Nation> nations) {
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
        case SETUP, RUNNING -> throw new IllegalStateException("Territories can only be selected during the selection phase");
        case SELECTION -> {
          if (players.stream().noneMatch(p -> p.identity().equals(identity))) {
            throw new IllegalStateException("Player is not present");
          }
          if(!players.getFirst().identity().equals(identity)) {
            throw new IllegalStateException("It is not that player's turn");
          }
          if (gameMap.graph().vertexSet().stream().noneMatch(t -> t.id().equals(id))) {
            throw new IllegalStateException("No such territory exists on the map"); // TODO: Say which territory
          }
          var territoryIds = nations.stream().map(Nation::territories).flatMap(Set::stream).collect(Collectors.toSet());
          if (territoryIds.contains(id)) {
            throw new IllegalStateException("That territory is already taken");
          }
          Nation nation = nations.stream().filter(n -> n.identity().equals(identity)).findAny().orElse(null);
          if (nation == null) {
            nation = new Nation(identity, new GameTerritory(id, territoryType));
            nations.add(nation);
          } else {
            nation.add(id, territoryType);
          }
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
