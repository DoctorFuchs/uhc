package de.hglabor.plugins.uhc.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public interface UHCPlayer {
    UUID getUuid();

    boolean isAlive();

    UserStatus getStatus();

    String getName();

    AtomicInteger getKills();

    void setStatus(UserStatus status);

    Optional<Player> getPlayer();

    Location getSpawnLocation();

    void setSpawnLocation(Location location);
}
