package com.riskrieg.core.api.game;

import com.riskrieg.core.api.color.GameColor;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.nation.Nation;
import com.riskrieg.core.api.game.player.Player;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public interface Game extends GameIdentifier {

  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 16;
  public static final double CLAIM_INCREASE_THRESHOLD = 5.0; // Threshold to gain another claim each turn
  public static final int MINIMUM_CLAIM_AMOUNT = 1;
  public static final int CAPITAL_ATTACK_ROLL_BOOST = 2;
  public static final int CAPITAL_DEFENSE_ROLL_BOOST = 1;

  @NonNull
  GameMode mode();

  @NonNull
  Instant creationTime();

  @NonNull
  Instant updatedTime();

  @NonNull
  GameState gameState();

  Set<Nation> nations();

  Set<Player> players();

  GameMap map();

  default Optional<Player> getPlayer(PlayerIdentifier identifier) {
    for (Player player : players()) {
      if (player.id().equals(identifier.id())) {
        return Optional.of(player);
      }
    }
    return Optional.empty();
  }

  default Optional<Nation> getNation(PlayerIdentifier identifier) {
    for (Nation nation : nations()) {
      if (nation.leaders().stream().anyMatch(leader -> leader.id().equals(identifier.id()))) {
        return Optional.of(nation);
      }
    }
    return Optional.empty();
  }

  default Optional<Nation> getNation(GameColor color) {
    for (Nation nation : nations()) {
      if (nation.color().equals(color)) {
        return Optional.of(nation);
      }
    }
    return Optional.empty();
  }

  GameAction<Boolean> setGameState(GameState gameState);

  GameAction<Player> addPlayer(PlayerIdentifier identifier, String name);

  GameAction<?> removePlayer(PlayerIdentifier identifier);

  GameAction<GameMap> selectMap(String codename);

  GameAction<GameMap> selectMap(GameMap map);

  GameAction<Nation> formNation(PlayerIdentifier identifier, GameColor color); // Select color and starting territory

  GameAction<?> addTerritory(Nation nation, TerritoryIdentifier territory, TerritoryIdentifier... territories); // TODO: Replace Nation with NationIdentifier?

  GameAction<?> removeTerritory(Nation nation, TerritoryIdentifier territory, TerritoryIdentifier... territories); // TODO: Replace Nation with NationIdentifier?

  GameAction<Player> start();

  GameAction<?> skip(PlayerIdentifier identifier);

  GameAction<?> claim();

}
