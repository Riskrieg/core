package com.riskrieg.core.api.game.map;

import com.riskrieg.core.api.game.map.territory.Border;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

public record GameMap(String codename, String displayName, String author,
                      Set<Territory> vertices, Set<Border> edges,
                      BufferedImage baseLayer, BufferedImage textLayer,
                      Options options) {

  @NonNull
  public static Optional<GameMap> load(Path path) {
    Objects.requireNonNull(path);
    // TODO: Implement
    return null;
  }

  @NonNull
  public static Optional<GameMap> load(URL url) {
    Objects.requireNonNull(url);
    // TODO: Implement
    return null;
  }

  public Graph<Territory, Border> graph() {
    var graph = new SimpleGraph<Territory, Border>(Border.class);
    for (Territory territory : vertices) {
      graph.addVertex(territory);
    }
    for (Border border : edges) {
      Territory source = vertices.stream().filter(t -> t.id().equals(border.source().id())).findAny().orElse(null);
      Territory target = vertices.stream().filter(t -> t.id().equals(border.target().id())).findAny().orElse(null);
      if (source != null && target != null) {
        graph.addEdge(source, target, border);
      }
    }
    return graph;
  }

  public boolean contains(TerritoryIdentifier identifier) {
    return graph().vertexSet().stream().anyMatch(territory -> territory.id().equals(identifier.id()));
  }

  public Optional<Territory> get(TerritoryIdentifier identifier) {
    for (Territory territory : vertices) {
      if (territory.id().equals(identifier.id())) {
        return Optional.of(territory);
      }
    }
    return Optional.empty();
  }

}
