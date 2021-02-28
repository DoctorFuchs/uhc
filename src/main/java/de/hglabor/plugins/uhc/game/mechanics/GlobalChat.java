package de.hglabor.plugins.uhc.game.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GlobalChat implements Listener {
    public final static GlobalChat INSTANCE = new GlobalChat();
    private boolean isEnabled;

    private GlobalChat() {
    }

    @EventHandler
    private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (isEnabled) return;
        event.setCancelled(!player.hasPermission("group.mod") && !player.isOp());
    }

    public void enable(boolean enable) {
        isEnabled = enable;
        if (enable) {
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "CHAT HAS BEEN ENABLED");
        } else {
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "CHAT HAS BEEN DISABLED");
        }
    }
}
