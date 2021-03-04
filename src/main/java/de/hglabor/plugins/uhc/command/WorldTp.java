package de.hglabor.plugins.uhc.command;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.permissions.ServerOperator;

public class WorldTp {
    public WorldTp() {
        new CommandAPICommand("worldtp")
                .withPermission("group.admin")
                .withRequirement(ServerOperator::isOp)
                .executesPlayer((player, objects) -> {
                    player.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
                }).register();
    }
}
