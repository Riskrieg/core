package com.riskrieg.gamerule;

public interface GameRule extends Comparable<GameRule> {

  String getName();

  String getDisplayName();

  boolean isEnabled();

  void setEnabled(boolean enabled);

  int getOrderID();

  @Override
  default int compareTo(GameRule o) {
    return Integer.compare(this.getOrderID(), o.getOrderID());
  }

}
