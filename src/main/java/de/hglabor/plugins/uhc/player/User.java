package de.hglabor.plugins.uhc.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements UHCPlayer {
    private final UUID uuid;
    private final String name;
    private final AtomicInteger kills;
    private Location spawn;
    private UserStatus status;

    public User(UUID uuid, String name) {
        this.status = UserStatus.LOBBY;
        this.kills = new AtomicInteger();
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean isAlive() {
        return status == UserStatus.INGAME || status == UserStatus.OFFLINE;
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AtomicInteger getKills() {
        return kills;
    }

    @Override
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    @Override
    public Location getSpawnLocation() {
        return spawn;
    }

    @Override
    public void setSpawnLocation(Location location) {
        spawn = location;
    }
}
