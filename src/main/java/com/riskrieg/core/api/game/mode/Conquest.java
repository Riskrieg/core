package com.riskrieg.core.api.game.mode;

import com.riskrieg.core.api.color.ColorBatch;
import com.riskrieg.core.api.color.GameColor;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GameState;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public final class Conquest implements Game {

  // Immutable
  private final GameIdentifier identifier;
  private final GameConstants constants;
  private final Instant creationTime;
  private Deque<Player> players;
  private Set<Nation> nations;

  // Mutable
  private ColorBatch colors;
  private Instant updatedTime;

  private GameState state;
  private GameMap map; // Nullable

  public Conquest(Save save) {
    this.identifier = save.identifier();
    this.constants = save.constants();
    this.colors = save.colors();
    this.creationTime = save.creationTime();
    this.updatedTime = save.updatedTime();
    this.state = save.state();
    if (!save.mapCodename().isBlank()) {
      // TODO: Load map
    }
    this.players = save.players();
    this.nations = save.nations();
  }

  public Conquest(GameIdentifier identifier, GameConstants constants, ColorBatch colors) {
    this.identifier = identifier;
    this.constants = constants;
    this.colors = colors;
    this.creationTime = Instant.now();
    this.updatedTime = Instant.now();
    this.state = GameState.SETUP;
    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
  }

  @NonNull
  @Override
  public GameIdentifier identifier() {
    return identifier;
  }

  @NonNull
  @Override
  public GameConstants constants() {
    return constants;
  }

  @NonNull
  @Override
  public ColorBatch colors() {
    return colors;
  }

  @NonNull
  @Override
  public Instant creationTime() {
    return creationTime;
  }

  @NonNull
  @Override
  public Instant updatedTime() {
    return updatedTime;
  }

  @NonNull
  @Override
  public GameState state() {
    return state;
  }

  @NonNull
  @Override
  public Deque<Player> players() {
    return (Deque<Player>) Collections.unmodifiableCollection(players); // TODO: Unsure if I should do this, come back to this
  }

  @NonNull
  @Override
  public Set<Nation> nations() {
    return Collections.unmodifiableSet(nations);
  }

  @Override
  public GameMap map() {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Boolean> setState(GameState state) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<GameMap> selectMap(GameMap map) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Player> addPlayer(PlayerIdentifier identifier, String name) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<?> removePlayer(PlayerIdentifier identifier) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Nation> createNation(GameColor color, PlayerIdentifier player) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<?> addTerritory(NationIdentifier nation, TerritoryIdentifier territory, TerritoryIdentifier... territories) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<?> removeTerritory(NationIdentifier nation, TerritoryIdentifier territory, TerritoryIdentifier... territories) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Player> start() {
    return null;
  }

  @NonNull
  @Override
  public GameAction<?> skip(PlayerIdentifier identifier) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<?> claim() {
    return null;
  }

}
