package de.hglabor.plugins.uhc.player;

import java.util.UUID;

public interface UHCPlayer {
    UUID getUuid();

    boolean isAlive();

    UserStatus getStatus();

    String getName();
}
