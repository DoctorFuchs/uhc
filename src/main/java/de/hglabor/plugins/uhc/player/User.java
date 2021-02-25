package de.hglabor.plugins.uhc.player;

import java.io.DataOutput;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements UHCPlayer {
    private final UUID uuid;
    private final String name;
    private final AtomicInteger kills;
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
}
