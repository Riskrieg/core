package com.riskrieg.api.gamemode;

import com.riskrieg.api.gamemode.order.TurnOrder;
import com.riskrieg.api.nation.Nation;
import com.riskrieg.api.player.Player;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.territory.Territory;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class Conquest implements Gamemode {

  private final UUID id;
  private final Instant creationTime;
  private Instant lastUpdated;
  private GameMap map;

  private Deque<Player> players;
  private Set<Nation> nations;
  private Set<Territory> capitals;

  public Conquest() {
    this.id = UUID.randomUUID();
    this.creationTime = Instant.now();
    this.lastUpdated = Instant.now();

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
    this.capitals = new HashSet<>();
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
  public Collection<Territory> capitals() {
    return Collections.unmodifiableCollection(capitals);
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
    }
  }

  @Override
  public void leave(Player player) {
    Objects.requireNonNull(player);
    players.remove(player);
  }

  @Override
  public void selectMap(GameMap map) {
    this.map = Objects.requireNonNull(map);
  }

  public void setCapital(Player player, Territory territory) {
    Nation nation = new Nation(player.identity(), territory);
    nations.add(nation);
  }

  @Override
  public void grant(Player player, Territory territory) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(territory);
  }

  @Override
  public void revoke(Player player, Territory territory) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(territory);
  }

  @Override
  public void start(TurnOrder order) {
    players = order.sort(players);
  }

  /* Private Methods */

}
