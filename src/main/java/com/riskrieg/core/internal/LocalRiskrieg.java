package com.riskrieg.core.internal;

import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public record LocalRiskrieg(Path path) implements Riskrieg {

  public LocalRiskrieg {
    Objects.requireNonNull(path);
    if (!Files.isDirectory(path)) {
      throw new IllegalStateException("The path provided must be a directory/folder.");
    }
  }

  @NonNull
  @Override
  public GameAction<Group> createGroup(GroupIdentifier identifier) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Group> retrieveGroup(GroupIdentifier identifier) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Collection<Group>> retrieveAllGroups() {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Boolean> deleteGroup(GroupIdentifier identifier) {
    return null;
  }

}
