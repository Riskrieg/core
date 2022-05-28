/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.api.game;

public record GameConstants(int minimumPlayers,
                            int maximumPlayers,
                            int initialClaimAmount,
                            int claimIncreaseThreshold,
                            int capitalAttackBonus,
                            int capitalDefenseBonus) { // TODO: Load these values from a file on start-up

  public static final int MINIMUM_PLAYERS = 2;
  public static final int MAXIMUM_PLAYERS = 16;

  public GameConstants {
    if (minimumPlayers < MINIMUM_PLAYERS) {
      throw new IllegalArgumentException("The minimum number of players cannot be less than 2.");
    } else if (minimumPlayers > maximumPlayers) {
      throw new IllegalArgumentException("The minimum number of players cannot be greater than the maximum number of players.");
    }
    if (maximumPlayers > MAXIMUM_PLAYERS) {
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
