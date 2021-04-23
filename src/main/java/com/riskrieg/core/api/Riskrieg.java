package com.riskrieg.core.api;

import com.riskrieg.core.gamemode.GameID;

// TODO: (?: Unsure) Implement Riskrieg Save (.rks) file format for saves.
public final class Riskrieg {

  public static final String NAME = "Riskrieg";
  public static final String VERSION = "2104.23a";

  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 16;

  public static final double CLAIM_INCREASE_THRESHOLD = 5.0; // Threshold to gain another claim each turn
  public static final int MINIMUM_CLAIM_AMOUNT = 1;

  public static final int CAPITAL_ATTACK_ROLL_BOOST = 1;
  public static final int CAPITAL_DEFENSE_ROLL_BOOST = 1;

  public Riskrieg() {

  }

  public Conquest getConquestById(String id) {
    GameID gameID = new GameID(id);
    return null;
  }

}
