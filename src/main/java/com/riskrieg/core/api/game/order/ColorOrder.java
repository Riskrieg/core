package com.riskrieg.core.api.game.order;

import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class ColorOrder implements DetailedTurnOrder {

  @NonNull
  @Override
  public String displayName() {
    return "Color";
  }

  @NonNull
  @Override
  public String description() {
    return "Players take turns according to their color, from top to bottom.";
  }

  @NonNull
  @Override
  public Deque<Player> getSorted(@NonNull Collection<Player> players, @NonNull Collection<Nation> nations) {
    List<Player> playerList = new ArrayList<>(players);
    List<Nation> nationList = new ArrayList<>(nations);
    nationList.sort(Comparator.comparingInt(Nation::colorId));

    List<Player> sortedPlayerList = new ArrayList<>();
    for (Nation nation : nationList) {
      var iterator = playerList.iterator();
      while (iterator.hasNext()) {
        Player player = iterator.next();
        if (player.identifier().equals(nation.leaderIdentifier())) {
          sortedPlayerList.add(player);
          iterator.remove();
          break;
        }
      }
    }
    return new ArrayDeque<>(sortedPlayerList);
  }

}
