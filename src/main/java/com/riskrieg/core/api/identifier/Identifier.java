package com.riskrieg.core.api.identifier;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Marks an entity with an identifier. Entities with identifiers have an id that uniquely identifies them.
 *
 * @since 3.0
 */
public sealed interface Identifier permits GroupIdentifier, GameIdentifier, PlayerIdentifier {

  @NonNull
  String id();

}
