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

package com.riskrieg.core.internal.action.running.claim;

import com.riskrieg.core.api.Dice;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.map.TerritoryType;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.constant.Constants;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.ClaimBundle;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public final class ConquestClaimAction implements Action<ClaimBundle> {

  private final Identity identity;
  private final Set<TerritoryId> ids;
  private final Identity currentTurnIdentity;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Collection<Nation> nations;

  public ConquestClaimAction(Identity identity, Set<TerritoryId> ids, Identity currentTurnIdentity, GameState gameState, GameMap gameMap, Collection<Nation> nations) {
    this.identity = identity;
    this.ids = ids;
    this.currentTurnIdentity = currentTurnIdentity;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super ClaimBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> {
          if (nations.stream().noneMatch(nation -> nation.identity().equals(identity))) {
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
          Nation nation = nations.stream().filter(n -> n.identity().equals(identity)).findAny().orElseThrow();

          var invalidTerritories = ids.stream().filter(id -> !gameMap.contains(id)).collect(Collectors.toSet());
          var ownedTerritories = ids.stream().filter(id -> nation.territories().stream().anyMatch(tid -> tid.equals(id))).collect(Collectors.toSet());
          var notBorderingTerritories = ids.stream().filter(id -> nation.territories().stream().noneMatch(tid -> gameMap.areNeighbors(tid, id))).collect(Collectors.toSet());
          var alliedTerritories = new HashSet<TerritoryId>();
          for (Nation potentialAlly : nations) {
            if (nation.isAllied(potentialAlly.identity()) && potentialAlly.isAllied(nation.identity())) {
              alliedTerritories.addAll(ids.stream().filter(id -> potentialAlly.territories().contains(id)).collect(Collectors.toSet()));
            }
          }

          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("Invalid territories: " + invalidTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          if (!ownedTerritories.isEmpty()) {
            throw new IllegalStateException("Territories already owned: " + ownedTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          if (!notBorderingTerritories.isEmpty()) {
            throw new IllegalStateException("Not bordering territories: " + notBorderingTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          if (!alliedTerritories.isEmpty()) {
            throw new IllegalStateException("Territories belong to allies: " + alliedTerritories.stream().map(TerritoryId::value).collect(Collectors.joining(", ")).trim());
          }
          int claims = nation.getClaimAmount(gameMap, nations);
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
                boolean wasCapital = defender.territoryIsOfType(id, TerritoryType.CAPITAL);
                defender.remove(id);
                if (wasCapital && defender.territories().size() > 0) { // Select new capital
                  Optional<TerritoryId> randomTerritory = defender.territories().stream().skip(new Random().nextInt(defender.territories().size())).findFirst();
                  randomTerritory.ifPresent(t -> {
                    defender.remove(t);
                    defender.add(t, TerritoryType.CAPITAL);
                  });
                }
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
            success.accept(new ClaimBundle(claimed, taken, defended));
          }
        }
        default -> throw new IllegalStateException("Claims can only be made while the game is active");
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
        if (attacker.territoryIsOfType(neighbor, TerritoryType.CAPITAL)) {
          attackRolls += 1 + Constants.CAPITAL_ATTACK_ROLL_BOOST;
        } else {
          attackRolls++;
        }
      } else if (defender.territories().contains(neighbor)) {
        defenseRolls++;
        if (defender.territoryIsOfType(id, TerritoryType.CAPITAL)) {
          defenseSides += 1 + Constants.CAPITAL_DEFENSE_ROLL_BOOST;
        }
      }
    }
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

}
