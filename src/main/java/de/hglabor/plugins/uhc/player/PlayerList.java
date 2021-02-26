package de.hglabor.plugins.uhc.player;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class PlayerList {
    public final static PlayerList INSTANCE = new PlayerList();
    private final Map<UUID, User> players;

    private PlayerList() {
        this.players = new HashMap<>();
    }

    public UHCPlayer getPlayer(Player player) {
        return players.computeIfAbsent(player.getUniqueId(), uuid -> new User(uuid, player.getName()));
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

    public void remove(UHCPlayer player) {
        remove(player.getUuid());
    }

    public List<UHCPlayer> getAllPlayers() {
        return new ArrayList<>(players.values());
    }

    public List<UHCPlayer> getAlivePlayers() {
        return players.values().stream().filter(User::isAlive).collect(Collectors.toList());
    }

    public List<UHCPlayer> getLobbyPlayers() {
        return players.values().stream().filter(user -> user.getStatus().equals(UserStatus.LOBBY)).collect(Collectors.toList());
    }

    public List<UHCPlayer> getScatteringPlayers() {
        return players.values().stream().filter(user -> user.getStatus().equals(UserStatus.SCATTERING)).collect(Collectors.toList());
    }
}
