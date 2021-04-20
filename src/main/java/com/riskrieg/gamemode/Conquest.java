package com.riskrieg.gamemode;

import com.riskrieg.gamemode.order.TurnOrder;
import com.riskrieg.gamemode.util.GameID;
import com.riskrieg.gamemode.util.Moment;
import com.riskrieg.nation.Nation;
import com.riskrieg.player.Identity;
import com.riskrieg.player.Player;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.GameTerritory;
import com.riskrieg.map.TerritoryType;
import com.riskrieg.map.territory.TerritoryId;
import com.riskrieg.map.vertex.Territory;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class Conquest implements Gamemode {

  private final GameID id;
  private final Moment creationTime;
  private Moment lastUpdated;
  private GameMap map;

  private Deque<Player> players;
  private Set<Nation> nations;

  public Conquest() {
    this.id = new GameID();
    this.creationTime = Moment.now();
    this.lastUpdated = Moment.now();

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
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
  public Collection<Player> players() {
    return Collections.unmodifiableCollection(players);
  }

  @Override
  public Collection<Nation> nations() {
    return Collections.unmodifiableCollection(nations);
  }

  @Override
  public GameMap map() {
    return map;
  }

  @Override
  public void join(Player player) {
    Objects.requireNonNull(player);
    if (!players.contains(player)) {
      players.add(player);
      setLastUpdated();
    }
  }

  @Override
  public void leave(Player player) {
    Objects.requireNonNull(player);
    getNation(player.identity()).ifPresent(nation -> nations.remove(nation));
    players.remove(player);
    setLastUpdated();
  }

  @Override
  public void selectMap(GameMap map) {
    this.map = Objects.requireNonNull(map);
    this.nations = new HashSet<>();
    setLastUpdated();
  }

  @Override
  public void setCapital(Player player, TerritoryId id) {
    Nation nation = new Nation(player.identity(), new GameTerritory(id, TerritoryType.CAPITAL));
    nations.add(nation);
  }

  @Override
  public void grant(Player player, TerritoryId id) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(id);
  }

  @Override
  public void revoke(Player player, TerritoryId id) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(id);
  }

  @Override
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
