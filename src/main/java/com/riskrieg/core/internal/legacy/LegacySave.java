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

package com.riskrieg.core.internal.legacy;

import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GamePhase;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.game.TimePoint;
import com.riskrieg.core.api.game.entity.alliance.Alliance;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.feature.Feature;
import com.riskrieg.core.api.game.feature.FeatureFlag;
import com.riskrieg.core.api.game.mode.Brawl;
import com.riskrieg.core.api.game.mode.Conquest;
import com.riskrieg.core.api.game.mode.Regicide;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.internal.legacy.nation.LegacyNation;
import com.riskrieg.core.internal.legacy.player.LegacyPlayer;
import com.riskrieg.core.internal.legacy.territory.LegacyGameTerritory;
import com.riskrieg.map.territory.TerritoryIdentity;
import com.riskrieg.palette.RkpPalette;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public record LegacySave(LegacyGameModeType gameType, LegacyGameId id,
                         LegacyTimePoint creationTime, LegacyTimePoint lastUpdated,
                         LegacyGamePhase gameState, String mapSimpleName,
                         Deque<LegacyPlayer> players, Set<LegacyNation> nations) {

  public Save toSave(GameIdentifier identifier) {
    GameConstants constants = GameConstants.standard();
    RkpPalette palette = RkpPalette.standard();
    TimePoint creationTime = this.creationTime.toTimePoint();
    TimePoint updatedTime = this.lastUpdated.toTimePoint();
    GamePhase phase = switch (this.gameState) {
      case ENDED -> GamePhase.ENDED;
      case SETUP -> GamePhase.SETUP;
      case RUNNING, SELECTION -> GamePhase.ACTIVE;
    };
    String mapCodename = this.mapSimpleName;
    if (mapCodename == null) {
      mapCodename = "";
    }

    Deque<Player> players = new ArrayDeque<>();
    Set<Nation> nations = new HashSet<>();
    for (LegacyPlayer player : this.players) {
      Nation nation = new Nation(NationIdentifier.uuid(), player.colorId().value(), PlayerIdentifier.of(player.identity().id()));
      players.add(player.toPlayer());
      nations.add(nation);
    }

    Set<Claim> claims = new HashSet<>();
    Set<Alliance> alliances = new HashSet<>();
    for (LegacyNation nation : this.nations) {
      for (LegacyGameTerritory legacyGameTerritory : nation.territories()) {
        TerritoryType territoryType = switch (legacyGameTerritory.type()) {
          case NORMAL, FORTIFIED -> TerritoryType.PLAIN;
          case CAPITAL -> TerritoryType.CAPITAL;
        };
        Optional<Nation> optNation = getNation(nation, nations);
        if (optNation.isPresent()) {
          Claim claim = new Claim(optNation.get().identifier(), new GameTerritory(new TerritoryIdentity(legacyGameTerritory.id().value()), territoryType));
          claims.add(claim);
        }
      }
    }

    Set<FeatureFlag> featureFlags = new HashSet<>();
    Class<? extends Game> type = switch (this.gameType) {
      case CONQUEST -> {
        featureFlags.add(new FeatureFlag(Feature.ALLIANCES, true));
        yield Conquest.class;
      }
      case REGICIDE -> {
        featureFlags.add(new FeatureFlag(Feature.ALLIANCES, false));
        yield Regicide.class;
      }
      case BRAWL -> {
        featureFlags.add(new FeatureFlag(Feature.ALLIANCES, true));
        yield Brawl.class;
      }
      default -> {
        featureFlags = new HashSet<>();
        yield Conquest.class;
      }
    };

    return new Save(
        identifier, constants, palette,
        creationTime, updatedTime, phase, mapCodename,
        players, nations, claims, alliances,
        featureFlags, type
    );
  }

  private Optional<Nation> getNation(LegacyNation legacyNation, Collection<Nation> nations) {
    PlayerIdentifier identifier = PlayerIdentifier.of(legacyNation.identity().id());
    return nations.stream().filter(nation -> nation.leaderIdentifier().equals(identifier)).findFirst();
  }

}
