package com.riskrieg.core.internal;

import com.riskrieg.core.api.Group;
import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.internal.action.CompletableAction;
import com.riskrieg.core.internal.action.GenericAction;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;

public final class FileFolderRiskrieg implements Riskrieg {

  private final Path repository;

  public FileFolderRiskrieg(Path repository) {
    this.repository = repository;
  }

  public Path getRepository() {
    return repository;
  }

  @Nonnull
  @Override
  public CompletableAction<Group> createGroup(@Nonnull String id) {
    try {
      Path groupPath = repository.resolve(id);
      Files.createDirectories(groupPath);
      return new GenericAction<>(new FileFolderGroup(groupPath));
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Nonnull
  @Override
  public CompletableAction<Group> retrieveGroupById(@Nonnull String id) {
    try {
      Path groupPath = repository.resolve(id);
      if (!Files.exists(groupPath)) {
        throw new FileNotFoundException("No saves exist");
      }
      return new GenericAction<>(new FileFolderGroup(groupPath));
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

}
