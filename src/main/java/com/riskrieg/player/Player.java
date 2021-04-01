package com.riskrieg.player;

import com.riskrieg.nation.Nation;

public interface Player extends Comparable<Player> {

  PlayerIdentifier getIdentifier();

  String getID();

  PlayerColor getColor();

  String getName();

  void setName(String string);

  boolean isLeader(Nation nation);

  default boolean isComputer() {
    return false;
  }

  @Override
  default int compareTo(Player o) {
    return this.getColor().compareTo(o.getColor());
  }

}
