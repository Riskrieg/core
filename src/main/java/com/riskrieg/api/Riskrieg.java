package com.riskrieg.api;

import com.aaronjyoder.util.json.adapters.RuntimeTypeAdapterFactory;
import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.constant.Constants;
import com.riskrieg.gamemode.Game;
import com.riskrieg.gamemode.GameMode;
import com.riskrieg.gamemode.conquest.Conquest;
import com.riskrieg.gamemode.creative.Creative;
import com.riskrieg.gamerule.GameRule;
import com.riskrieg.gamerule.rules.Alliances;
import com.riskrieg.gamerule.rules.JoinAnyTime;
import com.riskrieg.gamerule.rules.RandomTurnOrder;
import com.riskrieg.player.ComputerPlayer;
import com.riskrieg.player.HumanPlayer;
import com.riskrieg.player.Player;
import com.riskrieg.response.Response;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("unused")
public class Riskrieg {

  public Riskrieg() {
    GsonUtil.register(RuntimeTypeAdapterFactory.of(Game.class).with(Conquest.class).with(Creative.class));
    GsonUtil.register(RuntimeTypeAdapterFactory.of(Player.class).with(HumanPlayer.class).with(ComputerPlayer.class));
    GsonUtil.register(RuntimeTypeAdapterFactory.of(GameRule.class).with(Alliances.class).with(RandomTurnOrder.class).with(JoinAnyTime.class));
  }

  /**
   * Attempts to create a {@code game} given the specified {@link com.riskrieg.gamemode.GameMode}. It then saves the game in {@value com.riskrieg.constant.Constants#MAP_PATH}{@code
   * folderName}/{@code fileName}.json and returns a response based on whether it was successful or not.
   *
   * @param gameMode   The game mode that the created game should be.
   * @param folderName The folder name for the save file.
   * @param fileName   The file name for the save file.
   * @return A new {@link com.riskrieg.response.Response} object indicating the success of game creation. Fails if a game already exists or if there is a file I/O issue.
   */
  public Response create(GameMode gameMode, String folderName, String fileName) {
    if (folderName == null || fileName == null || folderName.isBlank() || fileName.isBlank()) {
      return new Response(false, "Invalid filepath.");
    }
    if (gameMode == null) {
      return new Response(false, "Invalid game mode. Please supply a valid game mode.");
    }
    return switch (gameMode) {
      case CONQUEST -> attemptCreate(new Conquest(), folderName, fileName);
      case CREATIVE -> attemptCreate(new Creative(), folderName, fileName);
    };
  }

  /**
   * Attempts to save an existing {@code game} in {@value com.riskrieg.constant.Constants#MAP_PATH}{@code folderName}/{@code fileName}.json, and then returns a response based on
   * whether it was successful or not.
   *
   * @param game       The game to be saved.
   * @param folderName The folder name for the save file.
   * @param fileName   The file name for the save file.
   * @return A new {@link com.riskrieg.response.Response} object indicating the success of game saving. Fails if there is a file I/O issue.
   */
  public Response save(Game game, String folderName, String fileName) {
    if (folderName == null || fileName == null || folderName.isBlank() || fileName.isBlank()) {
      return new Response(false, "Invalid filepath.");
    }
    if (game == null) {
      return new Response(false, "No game to save.");
    }
    try {
      GsonUtil.write(Constants.SAVE_PATH + folderName + "/", fileName + ".json", Save.class, new Save(game));
      return new Response(true);
    } catch (Exception e) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return new Response(false, sw.toString());
    }
  }

  /**
   * Attempts to match a save file to a {@code game} given the {@code folderName}, then saves the game if a match is found, the returns a response based on the success of the
   * operation.
   *
   * @param game       The game to save.
   * @param folderName The relative directory to look for a matching save game in.
   * @return A new {@link com.riskrieg.response.Response} object indicating the success of game saving. Fails if a matching game could not be found or if there is a file I/O issue.
   */
  public Response save(Game game, String folderName) {
    if (folderName == null || folderName.isBlank()) {
      return new Response(false, "Invalid filepath.");
    }
    File dir = new File(Constants.SAVE_PATH + folderName + "/");
    File[] files = dir.listFiles();

    if (files != null) {
      try {
        for (File file : files) {
          Optional<Game> optGame = load(folderName, file.getName().split("\\.")[0].trim());
          if (optGame.isPresent() && game.equals(optGame.get())) {
            return save(game, folderName, file.getName().split("\\.")[0].trim());
          }
        }
        return new Response(false, "Could not find matching save.");
      } catch (NullPointerException e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return new Response(false, sw.toString());
      }
    }
    return new Response(false, "Unable to save game.");
  }

  /**
   * Attempts to load an existing {@code game} from {@value com.riskrieg.constant.Constants#MAP_PATH}{@code folderName}/{@code fileName}.json, and then returns a response based on
   * whether it was successful or not.
   *
   * @param folderName The folder name for the save file.
   * @param fileName   The file name for the save file.
   * @return A {@link Game} object wrapped in {@link java.util.Optional} that either contains the game object if loaded successfully, or is empty if no game exists or there was a
   * file I/O issue.
   */
  public Optional<Game> load(String folderName, String fileName) {
    if (folderName == null || fileName == null || folderName.isBlank() || fileName.isBlank()) {
      return Optional.empty();
    }
    try {
      Save save = GsonUtil.read(Constants.SAVE_PATH + folderName + "/" + fileName + ".json", Save.class);
      return save == null ? Optional.empty() : Optional.of(save.game());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Attempts to load all saves found in the relative {@value com.riskrieg.constant.Constants#MAP_PATH}{@code folderName}/ directory.
   *
   * @param folderName The relative directory to look for save files in.
   * @return The set containing all {@link Game} objects loaded from the given folder. Returns an empty set if there are no saves or a file I/O issue occurs.
   */
  public Set<Game> loadSaves(String folderName) {
    if (folderName == null || folderName.isBlank()) {
      return new HashSet<>();
    }
    File dir = new File(Constants.SAVE_PATH + folderName + "/");
    File[] files = dir.listFiles();

    if (files != null) {
      Set<Game> saves = new HashSet<>();
      try {
        for (File file : files) {
          Optional<Game> optGame = load(folderName, file.getName().split("\\.")[0].trim());
          optGame.ifPresent(saves::add);
        }
        return saves;
      } catch (NullPointerException e) {
        return new HashSet<>();
      }
    }
    return new HashSet<>();
  }

  /**
   * Attempts to load all saves in the {@value com.riskrieg.constant.Constants#MAP_PATH} relative directory.
   *
   * @return The set containing all {@link Game} objects loaded from the {@value com.riskrieg.constant.Constants#MAP_PATH} relative directory. Returns an empty set if there are no
   * saves or a file I/O issue occurs.
   */
  public Set<Game> loadAllSaves() {
    File[] saveFolders = new File(Constants.SAVE_PATH).listFiles(File::isDirectory);
    Set<Game> result = new HashSet<>();
    if (saveFolders != null) {
      for (File saveFolder : saveFolders) {
        if (saveFolder != null) {
          Set<Game> games = loadSaves(saveFolder.getName());
          result.addAll(games);
        }
      }
    }
    return result;
  }

  /**
   * Attempts to delete an existing {@code game} in {@value com.riskrieg.constant.Constants#MAP_PATH}{@code folderName}/{@code fileName}.json, and then returns a response based on
   * whether it was successful or not.
   *
   * @param folderName The folder name for the save file.
   * @param fileName   The file name for the save file.
   * @return A new {@link com.riskrieg.response.Response} object indicating the success of deletion. Fails if there is no game to delete or if there is a file I/O issue.
   */
  public Response delete(String folderName, String fileName) {
    if (folderName == null || fileName == null || folderName.isBlank() || fileName.isBlank()) {
      return new Response(false, "Invalid filepath.");
    }
    try {
      File save = new File(Constants.SAVE_PATH + folderName + "/" + fileName + ".json");
      if (save.delete()) {
        File dir = new File(Constants.SAVE_PATH + folderName + "/");
        File[] files = dir.listFiles();
        if (files != null && files.length == 0) {
          dir.delete();
        }
        return new Response(true);
      } else {
        return new Response(false, "There is no game to end.");
      }
    } catch (Exception e) {
      return new Response(false, "Error deleting save. Please try again later.");
    }
  }

  /**
   * Attempts to delete all games in {@value com.riskrieg.constant.Constants#MAP_PATH} with the matching {@code uuid} supplied.
   *
   * @param uuid The ID of the game, given by a {@link java.util.UUID} object.
   * @return A new {@link com.riskrieg.response.Response} object indicating the success of deletion. Fails if there is no game to delete or if there is a file I/O issue.
   */
  public Response delete(UUID uuid) {
    File[] saveFolders = new File(Constants.SAVE_PATH).listFiles(File::isDirectory);
    if (saveFolders != null) {
      for (File saveFolder : saveFolders) {
        if (saveFolder != null) {
          File[] saveFiles = saveFolder.listFiles();
          if (saveFiles != null) {
            for (File saveFile : saveFiles) {
              if (saveFile != null && saveFile.isFile() && saveFile.getName().endsWith(".json")) {
                Optional<Game> optGame = load(saveFolder.getName(), saveFile.getName().replace(".json", "").trim());
                if (optGame.isPresent() && optGame.get().getId().equals(uuid)) {
                  return delete(saveFolder.getName(), saveFile.getName().replace(".json", "").trim());
                }
              }
            }
          }
        }
      }
    }
    return new Response(false, "There is no game to end.");
  }

  // Private methods

  private Response attemptCreate(Game game, String folderName, String fileName) {
    if (folderName == null || fileName == null || folderName.isBlank() || fileName.isBlank()) {
      return new Response(false, "Invalid filepath.");
    }
    if (game.isEnded()) {
      if (delete(folderName, fileName).success()) {
        if (save(game, folderName, fileName).success()) {
          return new Response(true);
        } else {
          return new Response(false, "File error while attempting to create a new game.");
        }
      } else {
        return new Response(false, "File error while attempting to delete an ended game.");
      }
    } else if (saveExists(folderName, fileName)) {
      return new Response(false, "A game already exists.");
    } else {
      if (save(game, folderName, fileName).success()) {
        return new Response(true);
      } else {
        return new Response(false, "File error while attempting to create a new game.");
      }
    }
  }

  private boolean saveExists(String folderName, String fileName) {
    return load(folderName, fileName).isPresent();
  }

}
