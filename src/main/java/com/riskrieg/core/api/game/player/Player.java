package com.riskrieg.core.api.game.player;

import com.riskrieg.core.api.identifier.PlayerIdentifier;
import edu.umd.cs.findbugs.annotations.NonNull;

public interface Player extends PlayerIdentifier {

  @NonNull
  String name();

}
