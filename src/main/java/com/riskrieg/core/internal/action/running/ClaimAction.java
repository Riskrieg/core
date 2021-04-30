package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.unsorted.Dice;
import com.riskrieg.core.unsorted.constant.Constants;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.map.GameMap;
import com.riskrieg.core.unsorted.nation.Nation;
import com.riskrieg.core.unsorted.player.Identity;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClaimAction implements Action<ClaimResult> {

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
          var ownedTerritories = ids.stream().filter(id -> nation.territories().stream().anyMatch(tid -> tid.equals(id))).collect(Collectors.toSet());
          var notBorderingTerritories = ids.stream().filter(id -> nation.territories().stream().noneMatch(tid -> gameMap.areNeighbors(tid, id))).collect(Collectors.toSet());

          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("Invalid territories: " + invalidTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          if (!ownedTerritories.isEmpty()) {
            throw new IllegalStateException("Territories already owned: " + ownedTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          if (!notBorderingTerritories.isEmpty()) {
            throw new IllegalStateException("Not bordering territories: " + notBorderingTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          int claims = getClaimAmount(nation);
          if (claims != ids.size()) {
            throw new IllegalStateException("Trying to claim " + ids.size() + (ids.size() == 1 ? " territory" : " territories")
                + " but must claim " + claims + (claims == 1 ? " territory" : " territories"));
          }

          Set<TerritoryId> claimed = new HashSet<>();
          Set<TerritoryId> taken = new HashSet<>();
          Set<TerritoryId> defended = new HashSet<>();

          for (TerritoryId id : ids) {
            var defender = getNation(id);
            if (defender != null) {
              if (attack(nation, defender, id)) {
                defender.remove(id);
                nation.add(id);
                taken.add(id);
              } else {
                defended.add(id);
              }
            } else {
              nation.add(id);
              claimed.add(id);
            }
          }

          if (success != null) {
            success.accept(new ClaimResult(claimed, taken, defended));
          }
        }
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

  private boolean attack(Nation attacker, Nation defender, TerritoryId id) {
    int attackRolls = 1;
    int defenseRolls = 1;
    int attackSides = 8;
    int defenseSides = 6;
    var neighbors = gameMap.getNeighbors(id);
    for (TerritoryId neighbor : neighbors) {
      if (attacker.territories().contains(neighbor)) {
        attackRolls++;
      } else if (defender.territories().contains(neighbor)) {
        defenseRolls++;
      }
    }
    // TODO: Configure capital boost
    // TODO: Configure not connected to capital debuff
    Dice attackDice = new Dice(attackSides, attackRolls);
    Dice defenseDice = new Dice(defenseSides, defenseRolls);
    int attackerMax = Arrays.stream(attackDice.roll()).summaryStatistics().getMax();
    int defenderMax = Arrays.stream(defenseDice.roll()).summaryStatistics().getMax();
    return attackerMax > defenderMax;
  }

  private Nation getNation(TerritoryId id) {
    return nations.stream().filter(nation -> nation.territories().contains(id)).findAny().orElse(null);
  }

  private int getClaimAmount(@Nonnull Nation nation) {
    int claims = Constants.MINIMUM_CLAIM_AMOUNT + (int) (Math.floor(nation.territories().size() / Constants.CLAIM_INCREASE_THRESHOLD));
    return Math.min(getClaimableTerritories(nation).size(), claims);
  }

  private Set<TerritoryId> getClaimableTerritories(@Nonnull Nation nation) {
    Set<TerritoryId> neighbors = nation.neighbors(gameMap);
    // TODO: Remove allied territories
    return neighbors;
  }

}

class ClaimResult {

  private final Set<TerritoryId> claimed;
  private final Set<TerritoryId> taken;
  private final Set<TerritoryId> defended;

  public ClaimResult(Set<TerritoryId> claimed, Set<TerritoryId> taken, Set<TerritoryId> defended) {
    this.claimed = claimed;
    this.taken = taken;
    this.defended = defended;
  }

  public Set<TerritoryId> claimed() {
    return Collections.unmodifiableSet(claimed);
  }

  public Set<TerritoryId> taken() {
    return Collections.unmodifiableSet(taken);
  }

  public Set<TerritoryId> defended() {
    return Collections.unmodifiableSet(defended);
  }

}