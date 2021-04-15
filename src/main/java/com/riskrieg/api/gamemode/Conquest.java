package com.riskrieg.api.gamemode;

import com.riskrieg.api.gamemode.order.TurnOrder;
import com.riskrieg.api.nation.Nation;
import com.riskrieg.api.player.Identity;
import com.riskrieg.api.player.Player;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.GameTerritory;
import com.riskrieg.map.TerritoryType;
import com.riskrieg.map.territory.TerritoryId;
import com.riskrieg.map.vertex.Territory;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Conquest implements Gamemode {

  private final UUID id;
  private final Instant creationTime;
  private Instant lastUpdated;
  private GameMap map;

  private Deque<Player> players;
  private Set<Nation> nations;

  public Conquest() {
    this.id = UUID.randomUUID();
    this.creationTime = Instant.now();
    this.lastUpdated = Instant.now();

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
  }

  @Override
  public UUID id() {
    return id;
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
    this.lastUpdated = Instant.now();
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
