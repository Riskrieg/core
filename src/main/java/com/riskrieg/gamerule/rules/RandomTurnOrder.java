package com.riskrieg.gamerule.rules;

import com.riskrieg.gamerule.GameRule;

public class RandomTurnOrder implements GameRule {

  private final String name;
  private boolean enabled;

  public RandomTurnOrder() {
    this.name = "random-turns";
    this.enabled = false;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDisplayName() {
    return "Random Turn Order";
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
    return 1;
  }

}
