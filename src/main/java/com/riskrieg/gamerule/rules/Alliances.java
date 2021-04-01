package com.riskrieg.gamerule.rules;

import com.riskrieg.gamerule.GameRule;

public class Alliances implements GameRule {

  private final String name;
  private boolean enabled;

  public Alliances() {
    this.name = "alliances";
    this.enabled = true;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDisplayName() {
    return "Alliances";
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public int getOrderID() {
    return 0;
  }

}
