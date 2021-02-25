package de.hglabor.plugins.uhc.player;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public interface UHCPlayer {
    UUID getUuid();

    boolean isAlive();

    UserStatus getStatus();

    String getName();

    AtomicInteger getKills();
}
