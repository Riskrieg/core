package com.riskrieg.core.api.game.map;

import com.riskrieg.core.api.game.map.territory.Border;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

public record GameMap(String codename, String displayName, String author,
                      Set<Territory> vertices, Set<Border> edges,
                      BufferedImage baseLayer,
                      BufferedImage textLayer) { // Two instances of the same GameMap loaded from a file won't report equal because BufferedImage equality is reference-based

  public static final String CODENAME_REGEX = "^(?!-)[a-z\\d-]+[^-]$";

  public static final Color BORDER_COLOR = new Color(116, 79, 40);
  public static final Color TEXT_COLOR = new Color(116, 79, 40);

  public static final Color TERRITORY_COLOR = new Color(224, 219, 227);
  public static final Color LAND_COLOR = new Color(200, 183, 173);

  public static final Color WATER_COLOR = new Color(192, 163, 146);
  public static final Color CONNECTION_COLOR = new Color(148, 125, 111);

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
