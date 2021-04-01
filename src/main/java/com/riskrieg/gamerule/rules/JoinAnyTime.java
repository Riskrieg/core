package com.riskrieg.gamerule.rules;

import com.riskrieg.gamerule.GameRule;

public class JoinAnyTime implements GameRule {

  private final String name;
  private boolean enabled;

  public JoinAnyTime() {
    this.name = "join-any-time";
    this.enabled = false;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDisplayName() {
    return "Join Any Time";
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
    return 2;
  }

}
