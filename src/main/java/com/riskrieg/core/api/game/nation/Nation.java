package com.riskrieg.core.api.game.nation;

import com.riskrieg.core.api.color.GameColor;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import java.util.Set;

public interface Nation {

  GameColor color();

  Set<PlayerIdentifier> leaders();

}
