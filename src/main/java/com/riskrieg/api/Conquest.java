package com.riskrieg.api;

import com.riskrieg.api.action.JoinAction;
import com.riskrieg.api.action.LeaveAction;
import com.riskrieg.gamemode.GameID;
import com.riskrieg.gamemode.GameMode;
import com.riskrieg.gamemode.GameState;
import com.riskrieg.gamemode.Moment;
import com.riskrieg.gamemode.order.TurnOrder;
import com.riskrieg.gamerule.GameRule;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.GameTerritory;
import com.riskrieg.map.TerritoryType;
import com.riskrieg.map.territory.TerritoryId;
import com.riskrieg.map.vertex.Territory;
import com.riskrieg.nation.Nation;
import com.riskrieg.player.Identity;
import com.riskrieg.player.Player;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public final class Conquest implements GameMode {

  private final GameID id;
  private final Moment creationTime;
  private Moment lastUpdated;

  private GameState gameState;
  private GameMap map;

  private final Map<GameRule, Boolean> gameRules;
  private Deque<Player> players;
  private Set<Nation> nations;

  public Conquest() {
    this.id = new GameID();
    this.creationTime = Moment.now();
    this.lastUpdated = Moment.now();
    this.gameState = GameState.SETUP;

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
    gameRules = new EnumMap<>(GameRule.class);
    gameRules.put(GameRule.ALLIANCES, true);
  }

  @Override
  public GameID id() {
    return id;
  }

  @Override
  public Moment creationTime() {
    return creationTime;
  }

  @Override
  public Moment lastUpdated() {
    return lastUpdated;
  }

  @Override
  public GameState gameState() {
    return gameState;
  }

  public Map<GameRule, Boolean> gameRules() {
    return Collections.unmodifiableMap(gameRules);
  }

  public Collection<Player> players() {
    return Collections.unmodifiableCollection(players);
  }

  public Collection<Nation> nations() {
    return Collections.unmodifiableCollection(nations);
  }

  public GameMap map() {
    return map;
  }

  @Nonnull
  @CheckReturnValue
  public JoinAction join(@Nonnull Identity id, @Nonnull String name, @Nonnull Color color) {
    setLastUpdated();
    return new JoinAction(id, name, color, players, gameState);
  }

  @Nonnull
  @CheckReturnValue
  public JoinAction join(@Nonnull String name, @Nonnull Color color) {
    return this.join(Identity.random(), name, color);
  }

  public LeaveAction leave(Player player) {
    setLastUpdated();
    return new LeaveAction(player, players, nations);
  }

  public void selectMap(GameMap map) {
    this.map = Objects.requireNonNull(map);
    this.nations = new HashSet<>();
    setLastUpdated();
  }

  public void setCapital(Player player, TerritoryId id) {
    Nation nation = new Nation(player.identity(), new GameTerritory(id, TerritoryType.CAPITAL));
    nations.add(nation);
  }

  public void grant(Player player, TerritoryId id) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(id);
  }

  public void revoke(Player player, TerritoryId id) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(id);
  }

  public void start(TurnOrder order) {
    players = order.sort(players);
  }

  /* Private Methods */

  private void setLastUpdated() {
    this.lastUpdated = Moment.now();
  }

  private Optional<Nation> getNation(Identity identity) {
    for (Nation n : nations) {
      if (n.getLeaderIdentity().equals(identity)) {
        return Optional.of(n);
      }
    }
    return Optional.empty();
  }

  private Set<GameTerritory> territories() {
    return nations.stream().map(Nation::territories).flatMap(Set::stream).collect(Collectors.toSet());
  }

  private Territory getTerritory(TerritoryId id) {
    for (Territory t : map.getGraph().vertices()) {
      if (t.id().equals(id)) {
        return t;
      }
    }
    return null;
  }

}
