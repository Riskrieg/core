package com.riskrieg.core.api;

import com.riskrieg.core.gamemode.GameID;
import com.riskrieg.core.gamemode.GameMode;
import com.riskrieg.core.gamemode.GameState;
import com.riskrieg.core.gamemode.Moment;
import com.riskrieg.core.gamerule.GameRule;
import com.riskrieg.core.internal.action.ClaimAction;
import com.riskrieg.core.internal.action.FormNationAction;
import com.riskrieg.core.internal.action.JoinAction;
import com.riskrieg.core.internal.action.LeaveAction;
import com.riskrieg.core.internal.action.SelectMapAction;
import com.riskrieg.core.internal.action.StartAction;
import com.riskrieg.core.map.GameMap;
import com.riskrieg.core.map.GameTerritory;
import com.riskrieg.core.nation.Nation;
import com.riskrieg.core.order.TurnOrder;
import com.riskrieg.core.player.Identity;
import com.riskrieg.core.player.Player;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import com.riskrieg.map.vertex.Territory;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public final class Conquest implements GameMode {

  private final GameID id;
  private final Moment creationTime;
  private Moment lastUpdated;

  private GameState gameState;
  private GameMap gameMap;

  private final Map<GameRule, Boolean> gameRules;
  private Deque<Player> players;
  private Set<Nation> nations;

  public Conquest() {
    this.id = GameID.random();
    this.creationTime = Moment.now();
    this.lastUpdated = Moment.now();
    this.gameState = GameState.SETUP;
    this.gameMap = new GameMap();

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
    gameRules = new EnumMap<>(GameRule.class);
    gameRules.put(GameRule.CAP_ALLIANCES, true);
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

  @Override
  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public boolean isEnded() {
    return gameState.equals(GameState.ENDED);
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
    return gameMap;
  }

  /* Setup */

  @Nonnull
  @CheckReturnValue
  public JoinAction join(@Nonnull Identity id, @Nonnull String name, @Nonnull Color color) {
    setLastUpdated();
    return new JoinAction(id, name, color, gameState, players);
  }

  @Nonnull
  @CheckReturnValue
  public JoinAction join(@Nonnull String name, @Nonnull Color color) {
    return this.join(Identity.random(), name, color);
  }

  @Nonnull
  @CheckReturnValue
  public LeaveAction leave(Player player) {
    setLastUpdated();
    return new LeaveAction(player, players, nations);
  }

  @Nonnull
  @CheckReturnValue
  public SelectMapAction selectMap(RkmMap rkmMap) {
    setLastUpdated();
    return new SelectMapAction(rkmMap, gameState, gameMap, nations);
  }

  @Nonnull
  @CheckReturnValue
  public FormNationAction formNation(Identity identity, TerritoryId id) {
    setLastUpdated();
    return new FormNationAction(identity, id, gameState, gameMap, players, nations);
  }

  @Nonnull
  @CheckReturnValue
  public FormNationAction formNation(Player player, TerritoryId id) {
    return this.formNation(player.identity(), id);
  }

  @Nonnull
  @CheckReturnValue
  public StartAction start(@Nonnull TurnOrder order) {
    players = order.sort(players);
    return new StartAction(this, gameMap, players, nations);
  }

  /* Running */

  @Nonnull
  @CheckReturnValue
  public ClaimAction claim(Identity identity, TerritoryId... ids) {
    setLastUpdated();
    return new ClaimAction(identity, Set.of(ids), players.getFirst().identity(), gameState, gameMap, nations);
  }

  @Nonnull
  @CheckReturnValue
  public ClaimAction claim(Player player, TerritoryId... ids) {
    return this.claim(player.identity(), ids);
  }



  /* Private Methods */

  private void setLastUpdated() {
    this.lastUpdated = Moment.now();
  }

  private Set<GameTerritory> territories() {
    return nations.stream().map(Nation::territories).flatMap(Set::stream).collect(Collectors.toSet());
  }

  private Territory getTerritory(TerritoryId id) {
    for (Territory t : gameMap.getGraph().vertexSet()) {
      if (t.id().equals(id)) {
        return t;
      }
    }
    return null;
  }

}
