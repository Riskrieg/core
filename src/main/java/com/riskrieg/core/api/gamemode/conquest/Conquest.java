package com.riskrieg.core.api.gamemode.conquest;

import com.riskrieg.core.api.Group;
import com.riskrieg.core.api.gamemode.Save;
import com.riskrieg.core.internal.action.running.ClaimAction;
import com.riskrieg.core.internal.action.setup.FormNationAction;
import com.riskrieg.core.internal.action.setup.JoinAction;
import com.riskrieg.core.internal.action.setup.LeaveAction;
import com.riskrieg.core.internal.action.setup.SelectMapAction;
import com.riskrieg.core.internal.action.setup.StartAction;
import com.riskrieg.core.internal.impl.GroupImpl;
import com.riskrieg.core.unsorted.gamemode.GameID;
import com.riskrieg.core.unsorted.gamemode.GameMode;
import com.riskrieg.core.unsorted.gamemode.GameModeType;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.map.GameMap;
import com.riskrieg.core.unsorted.map.MapOptions;
import com.riskrieg.core.unsorted.nation.Nation;
import com.riskrieg.core.unsorted.order.TurnOrder;
import com.riskrieg.core.unsorted.player.Identity;
import com.riskrieg.core.unsorted.player.Player;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public final class Conquest implements GameMode {

  private final GroupImpl group;
  private final GameID id;
  private final Instant creationTime;
  private Instant lastUpdated;

  private GameState gameState;
  private GameMap gameMap;

  //  private final Map<GameRule, Boolean> gameRules;
  private Deque<Player> players;
  private Set<Nation> nations;

  public Conquest(GroupImpl group) {
    this.group = group;
    this.id = GameID.random();
    this.creationTime = Instant.now();
    this.lastUpdated = Instant.now();
    this.gameState = GameState.SETUP;
    this.gameMap = new GameMap();

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
//    gameRules = new EnumMap<>(GameRule.class);
//    gameRules.put(GameRule.CAP_ALLIANCES, true);
  }

  public Conquest(GroupImpl group, Save save) {
    this.group = group;
    this.id = save.id();
    this.creationTime = save.creationTime().asInstant();
    this.lastUpdated = save.lastUpdated().asInstant();
    this.gameState = save.gameState();
    // TODO: Load map
    this.players = new ArrayDeque<>(save.players());
    this.nations = new HashSet<>(save.nations());
  }

  @Override
  public Group getGroup() {
    return group;
  }

  @Override
  public GameID getId() {
    return id;
  }

  @Override
  public GameModeType type() {
    return GameModeType.CONQUEST;
  }

  @Override
  public Instant creationTime() {
    return creationTime;
  }

  @Override
  public Instant lastUpdated() {
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

//  public Map<GameRule, Boolean> gameRules() {
//    return Collections.unmodifiableMap(gameRules);
//  }

  public Collection<Player> players() {
    return Collections.unmodifiableCollection(players);
  }

  public Collection<Nation> nations() {
    return Collections.unmodifiableCollection(nations);
  }

  public GameMap map() {
    return gameMap;
  } // TODO: Return unmodifiable version of GameMap

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
  public SelectMapAction selectMap(RkmMap rkmMap, MapOptions options) {
    setLastUpdated();
    return new SelectMapAction(rkmMap, options, gameState, gameMap, nations);
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
    this.lastUpdated = Instant.now();
  }

}
