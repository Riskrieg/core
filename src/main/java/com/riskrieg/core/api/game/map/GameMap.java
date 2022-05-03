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
                      BufferedImage baseLayer, BufferedImage textLayer) {

  public static final String CODENAME_REGEX = "^(?!-)[a-z\\d-]+[^-]$";

  public GameMap {
    Objects.requireNonNull(codename);
    Objects.requireNonNull(displayName);
    Objects.requireNonNull(author);
    Objects.requireNonNull(vertices);
    Objects.requireNonNull(edges);
    Objects.requireNonNull(baseLayer);
    Objects.requireNonNull(textLayer);
    if (codename.isBlank()) {
      throw new IllegalArgumentException("String 'codename' cannot be blank");
    }
    if (!codename.matches(CODENAME_REGEX)) {
      throw new IllegalArgumentException("String 'codename' must match the regex '" + CODENAME_REGEX + "'");
    }
    if (displayName.isBlank()) {
      throw new IllegalArgumentException("String 'author' cannot be blank");
    }
    if (author.isBlank()) {
      throw new IllegalArgumentException("String 'author' cannot be blank");
    }
    vertices = Set.copyOf(vertices);
    edges = Set.copyOf(edges);
  }

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
      Territory source = vertices.stream().filter(t -> t.id().equals(border.sourceId())).findAny().orElse(null);
      Territory target = vertices.stream().filter(t -> t.id().equals(border.targetId())).findAny().orElse(null);
      if (source != null && target != null) {
        graph.addEdge(source, target, border);
      }
    }
    return graph;
  }

  public boolean contains(TerritoryIdentifier identifier) {
    return vertices.stream().anyMatch(territory -> territory.id().equals(identifier.id()));
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
