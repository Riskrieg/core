package com.riskrieg.core.api.game.order;

import edu.umd.cs.findbugs.annotations.NonNull;

public interface DetailedTurnOrder extends TurnOrder {

  /**
   * The human-readable display name for this function.
   * Example: FullRandomTurnOrder could have the name "Full Random".
   *
   * @return The human-readable display name for this TurnOrder function.
   */
  @NonNull
  String displayName();

  /**
   * The description of how this TurnOrder implementation sorts the elements.
   * Example:
   *
   * @return The description of how this implementation sorts the elements.
   */
  @NonNull
  String description();

}
