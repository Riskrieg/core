package com.riskrieg.core.api.game.territory;

import com.riskrieg.core.api.identifier.NationIdentifier;

public record Claim(NationIdentifier identifier, GameTerritory territory) {

}
