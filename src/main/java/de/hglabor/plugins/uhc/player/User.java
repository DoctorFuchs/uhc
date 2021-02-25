package de.hglabor.plugins.uhc.player;

import java.util.UUID;

public class User implements UHCPlayer {
    private final UUID uuid;
    private final String name;
    private UserStatus status;

    public User(UUID uuid, String name) {
        this.status = UserStatus.LOBBY;
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
}
