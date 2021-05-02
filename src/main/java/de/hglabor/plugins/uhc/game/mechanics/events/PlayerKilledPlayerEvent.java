package de.hglabor.plugins.uhc.game.mechanics.events;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerKilledPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player killer;
    private final LivingEntity dead;
    private final UUID deadPlayerUUID;
    private final Location deathLocation;

    public PlayerKilledPlayerEvent(Player killer, LivingEntity dead, UUID deadPlayerUUID) {
        this.killer = killer;
        this.dead = dead;
        this.deathLocation = dead.getLocation();
        this.deadPlayerUUID = deadPlayerUUID;
    }

    public Player getKiller() {
        return killer;
    }

    public LivingEntity getDead() {
        return dead;
    }

    public Location getDeathLocation() {
        return deathLocation;
    }

    public UUID getDeadPlayerUUID() {
        return deadPlayerUUID;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
