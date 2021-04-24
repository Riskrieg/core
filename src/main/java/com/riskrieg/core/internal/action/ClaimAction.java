package com.riskrieg.core.internal.action;

import com.riskrieg.core.gamemode.GameState;
import com.riskrieg.core.map.GameMap;
import com.riskrieg.core.nation.Nation;
import com.riskrieg.core.player.Identity;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class ClaimAction implements GameAction<ClaimResult> {

  private final Identity identity;
  private final Set<TerritoryId> ids;
  private final Identity currentTurnIdentity;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Collection<Nation> nations;

  public ClaimAction(Identity identity, Set<TerritoryId> ids, Identity currentTurnIdentity, GameState gameState, GameMap gameMap, Collection<Nation> nations) {
    this.identity = identity;
    this.ids = ids;
    this.currentTurnIdentity = currentTurnIdentity;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super ClaimResult> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED, SETUP -> throw new IllegalStateException("Claims can only be made while the game is active");
        case RUNNING -> {
          if (nations.stream().noneMatch(nation -> nation.getLeaderIdentity().equals(identity))) {
            throw new IllegalStateException("Player is not present");
          }
          if (ids.isEmpty()) {
            throw new IllegalStateException("No territories supplied");
          }
          if (!identity.equals(currentTurnIdentity)) {
            throw new IllegalStateException("It is not that player's turn");
          }
          if (!gameMap.isSet()) {
            throw new IllegalStateException("The game map is not set");
          }
          Nation nation = nations.stream().filter(n -> n.getLeaderIdentity().equals(identity)).findAny().orElseThrow();

          var invalidTerritories = ids.stream().filter(id -> !gameMap.contains(id)).collect(Collectors.toSet());
          var ownedTerritories = ids.stream().filter(id -> nation.territories().stream().anyMatch(gt -> gt.id().equals(id))).collect(Collectors.toSet());
          var notBorderingTerritories = ids.stream().filter(id -> nation.territories().stream().noneMatch(gt -> gameMap.neighbors(gt.id(), id))).collect(Collectors.toSet());

          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("Invalid territories: " + invalidTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          if (!ownedTerritories.isEmpty()) {
            throw new IllegalStateException("Territories already owned: " + ownedTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          if (!notBorderingTerritories.isEmpty()) {
            throw new IllegalStateException("Not bordering territories: " + notBorderingTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          // TODO: Make sure they're claiming as many territories as they can. AP system?
          // TODO: Then process attack

          if (success != null) {
            success.accept(new ClaimResult());
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

class ClaimResult { // TODO: Add data for claimed/defended/taken

  public ClaimResult() {

  }

}