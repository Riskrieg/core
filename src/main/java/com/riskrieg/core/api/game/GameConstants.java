package com.riskrieg.core.api.game;

public record GameConstants(int minimumPlayers,
                            int maximumPlayers,
                            int initialClaimAmount,
                            int claimIncreaseThreshold,
                            int capitalAttackBonus,
                            int capitalDefenseBonus) { // TODO: Load these values from a file on start-up

  public GameConstants {
    if (minimumPlayers < 2) {
      throw new IllegalArgumentException("The minimum number of players cannot be less than 2.");
    } else if (minimumPlayers > maximumPlayers) {
      throw new IllegalArgumentException("The minimum number of players cannot be greater than the maximum number of players.");
    }
    if (maximumPlayers > 16) {
      throw new IllegalArgumentException("The maximum number of players cannot be greater than 16.");
    }
    if (initialClaimAmount < 0) {
      throw new IllegalArgumentException("The initial claim amount cannot be negative.");
    }
    if (claimIncreaseThreshold < 0) {
      throw new IllegalArgumentException("The claim increase threshold cannot be negative.");
    }
    if (capitalAttackBonus < 0) {
      throw new IllegalArgumentException("The capital attack bonus cannot be negative.");
    }
    if (capitalDefenseBonus < 0) {
      throw new IllegalArgumentException("The capital defense bonus cannot be negative.");
    }
  }

  public static GameConstants standard() {
    return new GameConstants(2, 16, 1, 5, 2, 1);
  }

}
