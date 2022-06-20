package com.riskrieg.core.internal.legacy.nation;

import com.riskrieg.core.internal.legacy.LegacyIdentity;
import com.riskrieg.core.internal.legacy.territory.LegacyGameTerritory;
import java.util.Set;

public record LegacyNation(LegacyIdentity identity, Set<LegacyGameTerritory> territories, Set<LegacyIdentity> allies) {

}
