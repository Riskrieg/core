package com.riskrieg.gamemode;

import com.riskrieg.gamemode.util.GameID;
import com.riskrieg.gamemode.util.Moment;

// TODO: Add Javadoc
public interface GameMode {

  GameID id();

  Moment creationTime();

  Moment lastUpdated();

}
