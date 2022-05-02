package com.riskrieg.core.api.group;

import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameMode;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;

/**
 * A {@link Group} is conceptually similar to a folder. {@link Group}s contain collections of {@link Game} objects.
 */
public interface Group extends GroupIdentifier {

  @NonNull
  @CheckReturnValue
  default GameAction<Game> createGame(GameMode mode) {
    return createGame(mode, GameIdentifier.uuid());
  }

  @NonNull
  @CheckReturnValue
  GameAction<Game> createGame(GameMode mode, GameIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Game> retrieveGame(GameIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Collection<Game>> retrieveAllGames();

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> saveGame(Game game);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> deleteGame(GameIdentifier identifier);

}
