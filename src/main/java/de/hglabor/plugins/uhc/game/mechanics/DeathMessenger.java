package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DeathMessenger {
    public DeathMessenger() {
    }

    public void broadcast(UHCPlayer killer, UHCPlayer dead) {
        String deadText = ChatColor.RED + dead.getName() + " " + "[" + dead.getKills() + "]";
        String killerText = ChatColor.GREEN + killer.getName() + " " + "[" + killer.getKills() + "]";
        String slainText = ChatColor.GRAY + " was slain by ";
        Bukkit.broadcastMessage(deadText + slainText + killerText);
    }

    public void broadcast(UHCPlayer dead) {
        String deadText = ChatColor.RED + dead.getName() + " " + "[" + dead.getKills() + "]";
        Bukkit.broadcastMessage(deadText + ChatColor.GRAY + " died");
    }

    public void broadcast(UHCPlayer dead, String deathMessage) {
        String deadText = ChatColor.RED + dead.getName() + " " + "[" + dead.getKills() + "]" + ChatColor.GRAY;
        Bukkit.broadcastMessage(deathMessage.replaceAll(dead.getName(), deadText));
    }
}
