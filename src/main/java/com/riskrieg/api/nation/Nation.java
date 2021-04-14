package com.riskrieg.api.nation;

import com.riskrieg.map.territory.Territory;
import java.util.Set;

public interface Nation {

  Set<Territory> territories();

  boolean add(Territory territory);

  boolean remove(Territory territory);

}
