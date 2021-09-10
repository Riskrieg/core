package com.riskrieg.core.api.order;

import com.riskrieg.core.api.player.Player;
import java.util.Comparator;

class StandardComparator implements Comparator<Player> {

  @Override
  public int compare(Player o1, Player o2) {
    return o1.colorId().compareTo(o2.colorId());
  }

}
