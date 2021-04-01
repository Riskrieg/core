package com.riskrieg.api;

import com.aaronjyoder.util.json.gson.GsonRuntimeTypeAdapterFactory;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.exception.ExceptionUtils;

@SuppressWarnings("unused")
public class Riskrieg {

  public Riskrieg() {
    GsonUtil.register(GsonRuntimeTypeAdapterFactory.of(Game.class).with(Conquest.class).with(Creative.class));
    GsonUtil.register(GsonRuntimeTypeAdapterFactory.of(Player.class).with(HumanPlayer.class).with(ComputerPlayer.class));
    GsonUtil.register(GsonRuntimeTypeAdapterFactory.of(GameRule.class).with(Alliances.class).with(RandomTurnOrder.class).with(JoinAnyTime.class));
  }

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
//      e.printStackTrace();
      return new Response(false, ExceptionUtils.getStackTrace(e));
    }
  }

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
        return new Response(false, ExceptionUtils.getStackTrace(e));
      }
    }
    return new Response(false, "Unable to save game.");
  }

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
