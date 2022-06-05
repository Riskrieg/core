package com.riskrieg.core.util.game;

import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.Territory;
import com.riskrieg.map.territory.Border;
import com.riskrieg.map.territory.TerritoryIdentity;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleGraph;

public class GameUtil {

  private GameUtil() {
  }

  /* Territory-related utilities */

  /**
   * Determines whether the provided territory exists on the provided map.
   *
   * @param identity The identifier of the territory
   * @param map      The map the territory should be searched for on
   * @return {@code true} if the territory can be found, otherwise {@code false}
   */
  public static boolean territoryExists(TerritoryIdentity identity, RkmMap map) {
    return map.vertices().stream().anyMatch(territory -> territory.identity().equals(identity));
  }

  /**
   * Determines whether the provided territory does not exist on the provided map.
   *
   * @param identity The identifier of the territory
   * @param map      The map the territory should be searched for on
   * @return {@code true} if the territory cannot be found, otherwise {@code false}
   */
  public static boolean territoryNotExists(TerritoryIdentity identity, RkmMap map) {
    return map.vertices().stream().noneMatch(territory -> territory.identity().equals(identity));
  }

  /**
   * Determines whether the provided territory has been claimed by any nation.
   *
   * @param identity  The identifier of the territory
   * @param allClaims The set of claims to search through
   * @return {@code true} if the territory is claimed by a nation, otherwise {@code false}
   */
  public static boolean territoryIsClaimed(TerritoryIdentity identity, Set<Claim> allClaims) {
    return allClaims.stream().anyMatch(claim -> claim.territory().identity().equals(identity));
  }

  /**
   * Determines whether a territory is of the provided type.
   *
   * @param identity  The identifier of the territory
   * @param type      The type to check the provided territory against
   * @param allClaims The set of claims to search through
   * @return {@code true} if the territory is of the provided type, otherwise {@code false}
   */
  public static boolean territoryIsOfType(TerritoryIdentity identity, TerritoryType type, Set<Claim> allClaims) {
    var claim = allClaims.stream().filter(c -> c.territory().identity().equals(identity)).findFirst();
    return claim.isPresent() && claim.get().territory().type().equals(type);
  }

  public static Set<Territory> getNeighbors(TerritoryIdentity identity, RkmMap map) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(map);
    var territory = map.get(identity);
    if (territory.isPresent()) {
      var graph = mapToGraph(map);
      return Graphs.neighborSetOf(graph, territory.get());
    }
    return new HashSet<>();
  }

  private static Graph<Territory, Border> mapToGraph(RkmMap map) {
    var graph = new SimpleGraph<Territory, Border>(Border.class);
    for (Territory territory : map.vertices()) {
      graph.addVertex(territory);
    }
    for (Border border : map.edges()) {
      Territory source = map.vertices().stream().filter(t -> t.identity().equals(border.source())).findAny().orElse(null);
      Territory target = map.vertices().stream().filter(t -> t.identity().equals(border.target())).findAny().orElse(null);
      if (source != null && target != null) {
        graph.addEdge(source, target, border);
      }
    }
    return graph;
  }


}
