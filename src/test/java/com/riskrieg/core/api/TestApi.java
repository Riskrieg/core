package com.riskrieg.core.api;

import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.mode.StandardMode;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import java.nio.file.Path;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class TestApi { // TODO: Implement comprehensive tests

  @Test
  public void testRepositoryOperations() {
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/saves/")).build();
    Collection<Group> groups = api.retrieveAllGroups().complete();
    System.out.println(groups.size());
  }

  public void createLocalGroup() {
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/saves/")).build();
    Group group = api.createGroup(GroupIdentifier.of("12345")).complete();
  }

  public void createLocalGame() {
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/saves/")).build();
    Group group = api.createGroup(GroupIdentifier.of("12345")).complete();

    Game game = group.createGame(StandardMode.CONQUEST, GameIdentifier.of("123")).complete();
  }

}
