package com.riskrieg.core.api.game.order;

import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class RandomOrder implements DetailedTurnOrder {

  @NonNull
  @Override
  public String displayName() {
    return "Random";
  }

  @NonNull
  @Override
  public String description() {
    return "Players take turns in a randomized order.";
  }

  @NonNull
  @Override
  public Deque<Player> getSorted(@NonNull Collection<Player> players, @NonNull Collection<Nation> nations) {
    List<Player> list = new ArrayList<>(players);
    Collections.shuffle(list);
    return new ArrayDeque<>(list);
  }

}
