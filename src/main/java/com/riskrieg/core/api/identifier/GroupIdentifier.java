package com.riskrieg.core.api.identifier;

import com.riskrieg.core.internal.identifier.CustomGroupIdentifier;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.UUID;

public non-sealed interface GroupIdentifier extends Identifier {

  @NonNull
  static GroupIdentifier of(String id) {
    return new CustomGroupIdentifier(id);
  }

  @NonNull
  static GroupIdentifier uuid() {
    return new CustomGroupIdentifier(UUID.randomUUID().toString());
  }

}
