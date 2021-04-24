package com.riskrieg.core.api;

import com.aaronjyoder.util.json.adapters.RuntimeTypeAdapterFactory;
import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.gamemode.GameID;
import com.riskrieg.core.gamemode.GameMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;

// TODO: (?: Unsure) Implement Riskrieg Save (.rks) file format for saves.
public final class Riskrieg {

  public static final String NAME = "Riskrieg";
  public static final String VERSION = "2104.23a";

  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 16;

  public static final double CLAIM_INCREASE_THRESHOLD = 5.0; // Threshold to gain another claim each turn
  public static final int MINIMUM_CLAIM_AMOUNT = 1;

  public static final int CAPITAL_ATTACK_ROLL_BOOST = 1;
  public static final int CAPITAL_DEFENSE_ROLL_BOOST = 1;

  public Riskrieg() {
    GsonUtil.register(RuntimeTypeAdapterFactory.of(GameMode.class).with(Conquest.class));
  }

  @Nonnull
  public <T extends GameMode> Optional<T> create(@Nonnull Path filePath, @Nonnull Class<T> type) {
    if (!Files.isRegularFile(filePath)) {
      return Optional.empty();
    }
    try {
      var optGame = load(filePath);
      if (optGame.isEmpty()) {
        return Optional.of(type.getDeclaredConstructor().newInstance());
      }
      if (!optGame.get().isEnded()) {
        return Optional.empty();
      }
      var newGame = type.getDeclaredConstructor().newInstance();
      if (delete(filePath) && save(filePath, newGame)) {
        return Optional.of(newGame);
      }
    } catch (Exception e) {
      return Optional.empty();
    }
    return Optional.empty();
  }

  public boolean delete(@Nonnull Path filePath) {
    if (!Files.isRegularFile(filePath)) {
      return false;
    }
    try {
      if (Files.deleteIfExists(filePath) && Files.isDirectory(filePath.getParent()) && Files.list(filePath.getParent()).findAny().isEmpty()) {
        return Files.deleteIfExists(filePath.getParent());
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean delete(@Nonnull GameID id) {
    // TODO: Implement
    return false;
  }

  public boolean save(@Nonnull Path path, @Nonnull GameMode gameMode) {
    // TODO: Handle both cases of whether it's a folder or a file
    try {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Nonnull
  public Optional<GameMode> load(@Nonnull Path filePath) {
    if (!Files.isRegularFile(filePath)) {
      return Optional.empty();
    }
    // TODO: Implement
    return Optional.empty();
  }

  @Nonnull
  public <T extends GameMode> Optional<T> load(@Nonnull Path filePath, @Nonnull Class<T> type) {
    if (!Files.isRegularFile(filePath)) {
      return Optional.empty();
    }
    // TODO: Implement
    return Optional.empty();
  }

  @Nonnull
  public Optional<GameMode> loadById(@Nonnull GameID gameID) {
    // TODO: Implement
    return Optional.empty();
  }

  @Nonnull
  public <T extends GameMode> Optional<T> loadById(@Nonnull GameID gameID, @Nonnull Class<T> type) {
    // TODO: Implement
    return Optional.empty();
  }

  @Nonnull
  public Set<GameMode> loadAll(@Nonnull Path folderPath) {
    if (!Files.isDirectory(folderPath)) {
      return new HashSet<>();
    }
    // TODO: Implement
    return new HashSet<>();
  }

  @Nonnull
  public Set<GameMode> loadAll() {
    // TODO: Implement
    return new HashSet<>();
  }

  /* Private Methods */

}
