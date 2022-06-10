/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
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

package com.riskrieg.core.api.game;

import com.riskrieg.core.api.game.entity.alliance.Alliance;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.event.AllianceEvent;
import com.riskrieg.core.api.game.event.ClaimEvent;
import com.riskrieg.core.api.game.event.UpdateEvent;
import com.riskrieg.core.api.game.feature.Feature;
import com.riskrieg.core.api.game.feature.FeatureFlag;
import com.riskrieg.core.api.game.feature.alliance.AllianceStatus;
import com.riskrieg.core.api.game.order.TurnOrder;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import com.riskrieg.core.internal.requests.GenericAction;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryIdentity;
import com.riskrieg.palette.RkpColor;
import com.riskrieg.palette.RkpPalette;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public interface Game { // TODO: Action to rename player

  @NonNull
  GameIdentifier identifier();

  @NonNull
  GameConstants constants();

  @NonNull
  RkpPalette palette();

  @NonNull
  Instant creationTime();

  @NonNull
  Instant updatedTime();

  @NonNull
  GamePhase phase();

  @Nullable
  RkmMap map();

  @NonNull
  Collection<Player> players();

  @NonNull
  Set<Nation> nations();

  @NonNull
  Set<Claim> claims();

  @NonNull
  Set<Alliance> alliances();

  Set<FeatureFlag> featureFlags();

  boolean isFeatureEnabled(Feature feature);

  @NonNull
  default Optional<Player> getPlayer(PlayerIdentifier identifier) {
    return players().stream()
        .filter(p -> p.identifier().equals(identifier))
        .findFirst();
  }

  @NonNull
  default Optional<Nation> getNation(PlayerIdentifier identifier) {
    return nations().stream()
        .filter(n -> n.leaderIdentifier().equals(identifier))
        .findFirst();
  }

  @NonNull
  default Optional<Nation> getNation(NationIdentifier identifier) {
    return nations().stream()
        .filter(n -> n.identifier().equals(identifier))
        .findFirst();
  }

  @NonNull
  default Optional<Nation> getNation(RkpColor color) {
    return nations().stream()
        .filter(n -> n.colorId() == color.order())
        .findFirst();
  }

  @NonNull
  default Optional<Nation> getNation(TerritoryIdentity identity) {
    return nations().stream().filter(nation -> nation.hasClaimOn(identity, claims())).findFirst();
  }

  default Set<Nation> getAllies(PlayerIdentifier identifier) {
    var nationOpt = getNation(identifier);
    if (nationOpt.isEmpty()) {
      return Set.of();
    }
    Set<Nation> allies = new HashSet<>();
    Nation nation = nationOpt.get();
    for (Nation n : nations()) {
      if (allianceStatus(nation.identifier(), n.identifier()).equals(AllianceStatus.COMPLETE)) {
        allies.add(n);
      }
    }
    return Collections.unmodifiableSet(allies);
  }

  default Set<Nation> getAllies(NationIdentifier identifier) {
    var nationOpt = getNation(identifier);
    if (nationOpt.isEmpty()) {
      return Set.of();
    }
    Set<Nation> allies = new HashSet<>();
    Nation nation = nationOpt.get();
    for (Nation n : nations()) {
      if (allianceStatus(nation.identifier(), n.identifier()).equals(AllianceStatus.COMPLETE)) {
        allies.add(n);
      }
    }
    return Collections.unmodifiableSet(allies);
  }

  default Set<Nation> getAllies(RkpColor color) {
    var nationOpt = getNation(color);
    if (nationOpt.isEmpty()) {
      return Set.of();
    }
    Set<Nation> allies = new HashSet<>();
    Nation nation = nationOpt.get();
    for (Nation n : nations()) {
      if (allianceStatus(nation.identifier(), n.identifier()).equals(AllianceStatus.COMPLETE)) {
        allies.add(n);
      }
    }
    return Collections.unmodifiableSet(allies);
  }

  @NonNull
  Optional<Player> getCurrentPlayer();

  @NonNull
  Optional<Nation> getCurrentNation();

  @NonNull
  default Optional<Claim> getClaim(TerritoryIdentity identity) {
    return claims().stream().filter(claim -> claim.territory().identity().equals(identity)).findFirst();
  }

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> setPalette(RkpPalette palette);

  @NonNull
  @CheckReturnValue
  GameAction<RkmMap> selectMap(RkmMap map);

  @NonNull
  @CheckReturnValue
  GameAction<Player> addPlayer(PlayerIdentifier identifier, String name);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> removePlayer(PlayerIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Nation> createNation(RkpColor color, PlayerIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> dissolveNation(RkpColor color);

  @NonNull
  @CheckReturnValue
  GameAction<ClaimEvent> claim(Attack attack, NationIdentifier identifier, ClaimOverride override, TerritoryIdentity... territories);

  @NonNull
  @CheckReturnValue
  default GameAction<ClaimEvent> claim(Attack attack, NationIdentifier identifier, TerritoryIdentity... territories) {
    return claim(attack, identifier, ClaimOverride.NONE, territories);
  }

  default GameAction<ClaimEvent> claim(Attack attack, PlayerIdentifier identifier, ClaimOverride override, TerritoryIdentity... territories) {
    Objects.requireNonNull(identifier);
    Optional<Nation> nation = getNation(identifier);
    return nation.map(n -> claim(attack, n.identifier(), override, territories))
        .orElseGet(() -> new GenericAction<>(new IllegalStateException("Unable to find nation with that player")));
  }

  default GameAction<ClaimEvent> claim(Attack attack, PlayerIdentifier identifier, TerritoryIdentity... territories) {
    return claim(attack, identifier, ClaimOverride.NONE, territories);
  }

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> unclaim(NationIdentifier identifier, TerritoryIdentity... territories);

  @NonNull
  @CheckReturnValue
  GameAction<Player> start(TurnOrder order, boolean reverse, boolean randomizeStart);

  @NonNull
  @CheckReturnValue
  default GameAction<Player> start(TurnOrder order) {
    return start(order, false, false);
  }

  @NonNull
  @CheckReturnValue
  GameAction<UpdateEvent> update(boolean advanceTurn);

  GameAction<AllianceEvent> ally(NationIdentifier ally, NationIdentifier coally);

  default GameAction<AllianceEvent> ally(PlayerIdentifier ally, PlayerIdentifier coally) {
    var allyNation = getNation(ally);
    var coallyNation = getNation(coally);
    if (allyNation.isEmpty() || coallyNation.isEmpty()) {
      return new GenericAction<>(new IllegalStateException("One of the players provided is not in the game."));
    }
    return ally(allyNation.get().identifier(), coallyNation.get().identifier());
  }

  GameAction<AllianceEvent> unally(NationIdentifier ally, NationIdentifier coally);

  default GameAction<AllianceEvent> unally(PlayerIdentifier ally, PlayerIdentifier coally) {
    var allyNation = getNation(ally);
    var coallyNation = getNation(coally);
    if (allyNation.isEmpty() || coallyNation.isEmpty()) {
      return new GenericAction<>(new IllegalStateException("One of the players provided is not in the game."));
    }
    return unally(allyNation.get().identifier(), coallyNation.get().identifier());
  }

  AllianceStatus allianceStatus(NationIdentifier ally, NationIdentifier coally);

  default AllianceStatus allianceStatus(PlayerIdentifier ally, PlayerIdentifier coally) {
    var allyNation = getNation(ally);
    var coallyNation = getNation(coally);
    if (allyNation.isEmpty() || coallyNation.isEmpty()) {
      return AllianceStatus.NONE;
    }
    return allianceStatus(allyNation.get().identifier(), coallyNation.get().identifier());
  }

}
