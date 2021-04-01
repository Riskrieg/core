package com.riskrieg.nation;

import com.riskrieg.player.PlayerIdentifier;
import java.util.Set;

public interface AllianceNation extends Nation {

  Set<PlayerIdentifier> getAllies();

  boolean isAlly(AllianceNation nation);

  boolean addAlly(AllianceNation nation);

  boolean addAlly(PlayerIdentifier identifier);

  boolean removeAlly(AllianceNation nation);

  boolean removeAlly(PlayerIdentifier identifier);

}
