package com.riskrieg.core.api;

import com.aaronjyoder.util.json.adapters.RuntimeTypeAdapterFactory;
import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.gamemode.GameID;
import com.riskrieg.core.gamemode.GameMode;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
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

  public <T extends GameMode> T create(String folderName, String fileName, Class<T> type)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    if (folderName != null && fileName != null && !folderName.isBlank() && !fileName.isBlank() && type != null) {
      // TODO: Check if game already exists, if it does and is ended, delete and save new game, then return it. If it exists and is not ended, return null;
      return type.getDeclaredConstructor().newInstance();
    }
    return null;
  }

  public void save(String folderName, String fileName, GameMode gameMode) {
    if (folderName != null && fileName != null && !folderName.isBlank() && !fileName.isBlank() && gameMode != null) {
//      GsonUtil.write(Constants.SAVE_PATH + folderName + "/", fileName + ".json", Save.class, new Save(game));
    }
  }

  public void save(String folderName, GameMode gameMode) {
    
  }

  public GameMode load(String folderName, String fileName) {
    return null;
  }

  public <T extends GameMode> T load(String folderName, String fileName, Class<T> type) {
    return null;
  }

  public GameMode loadById(GameID gameID) {
    return null;
  }

  public <T extends GameMode> T loadById(GameID gameID, Class<T> type) {
    return null;
  }

  public Set<GameMode> loadAll(String folderName) {
    return new HashSet<>();
  }

  public Set<GameMode> loadAll() {
    return new HashSet<>();
  }

  public void delete(String folderName, String fileName) {

  }

  public void delete(GameID id) {

  }

  /* Private Methods */

}
