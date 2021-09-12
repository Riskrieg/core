package com.riskrieg.core.constant;

import com.riskrieg.core.constant.color.ColorBatch;
import com.riskrieg.core.constant.color.ColorId;
import com.riskrieg.core.constant.color.PlayerColor;

public final class Constants {

  public static final String NAME = "Riskrieg";
  public static final String VERSION = "2109.11a";
  public static final String SAVE_FILE_EXT = ".json";

  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 16;

  public static final double CLAIM_INCREASE_THRESHOLD = 5.0; // Threshold to gain another claim each turn
  public static final int MINIMUM_CLAIM_AMOUNT = 1;

  public static final int CAPITAL_ATTACK_ROLL_BOOST = 2;
  public static final int CAPITAL_DEFENSE_ROLL_BOOST = 1;

  public static final ColorBatch DEFAULT_PLAYER_COLORS = new ColorBatch(
      new PlayerColor(ColorId.of(0), "Salmon", 255, 140, 150), new PlayerColor(ColorId.of(1), "Lavender", 155, 120, 190),
      new PlayerColor(ColorId.of(2), "Thistle", 215, 190, 240), new PlayerColor(ColorId.of(3), "Ice", 195, 230, 255),
      new PlayerColor(ColorId.of(4), "Sky", 120, 165, 215), new PlayerColor(ColorId.of(5), "Sea", 140, 225, 175),
      new PlayerColor(ColorId.of(6), "Forest", 85, 155, 60), new PlayerColor(ColorId.of(7), "Sod", 170, 190, 95),
      new PlayerColor(ColorId.of(8), "Cream", 255, 254, 208), new PlayerColor(ColorId.of(9), "Sun", 240, 225, 80),
      new PlayerColor(ColorId.of(10), "Gold", 255, 195, 5), new PlayerColor(ColorId.of(11), "Cadmium", 250, 105, 65),
      new PlayerColor(ColorId.of(12), "Sanguine", 95, 10, 0), new PlayerColor(ColorId.of(13), "Mocha", 75, 40, 0),
      new PlayerColor(ColorId.of(14), "Matte", 30, 30, 30), new PlayerColor(ColorId.of(15), "Cobalt", 0, 50, 120)
  );

}
