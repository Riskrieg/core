package com.riskrieg.core.api.game;

import com.riskrieg.core.api.identifier.GameIdentifier;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;

public interface Game extends GameIdentifier {

  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 16;
  public static final double CLAIM_INCREASE_THRESHOLD = 5.0; // Threshold to gain another claim each turn
  public static final int MINIMUM_CLAIM_AMOUNT = 1;
  public static final int CAPITAL_ATTACK_ROLL_BOOST = 2;
  public static final int CAPITAL_DEFENSE_ROLL_BOOST = 1;

  @NonNull
  GameMode mode();

  @NonNull
  Instant creationTime();

  @NonNull
  Instant updatedTime();

}
