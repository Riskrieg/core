package com.riskrieg.core.api.gamemode;

import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.AllianceBundle;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface AlliableMode extends GameMode {

  boolean allied(Identity identity1, Identity identity2);

  @Nonnull
  @CheckReturnValue
  Action<AllianceBundle> ally(Identity identity1, Identity identity2);

  @Nonnull
  @CheckReturnValue
  Action<AllianceBundle> unally(Identity identity1, Identity identity2);

}
