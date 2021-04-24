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

  public <T extends GameMode> Optional<T> create(Path filePath, Class<T> type) {
    if (filePath == null || !Files.isRegularFile(filePath) || type == null) {
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

  public boolean delete(Path filePath) {
    if (filePath == null || !Files.isRegularFile(filePath)) {
      return false;
    }
    try {
      // TODO: Delete directory if it is empty
      return Files.deleteIfExists(filePath);
    } catch (Exception e) {
      return false;
    }
  }

  public boolean delete(GameID id) {
    return false;
  }

  public boolean save(Path path, GameMode gameMode) {
    if (path == null || gameMode == null) {
      return false;
    }
    // TODO: Handle both cases of whether it's a folder or a file
    try {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Optional<GameMode> load(Path filePath) {
    if (filePath == null || !Files.isRegularFile(filePath)) {
      return Optional.empty();
    }
    return Optional.empty();
  }

  public <T extends GameMode> Optional<T> load(Path filePath, Class<T> type) {
    if (filePath == null || !Files.isRegularFile(filePath)) {
      return Optional.empty();
    }
    return Optional.empty();
  }

  public Optional<GameMode> loadById(GameID gameID) {
    return Optional.empty();
  }

  public <T extends GameMode> Optional<T> loadById(GameID gameID, Class<T> type) {
    return Optional.empty();
  }

  public Set<GameMode> loadAll(Path folderPath) {
    if (folderPath == null || !Files.isDirectory(folderPath)) {
      return new HashSet<>();
    }
    return new HashSet<>();
  }

  public Set<GameMode> loadAll() {
    return new HashSet<>();
  }

  /* Private Methods */

}
