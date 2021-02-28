package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scoreboard.Objective;

public class HeartDisplay implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            updateHeal((Player) event.getEntity());
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            updateHeal((Player) event.getEntity());
        }
    }

    private void updateHeal(Player player) {
        UHCPlayer uhcPlayer = PlayerList.INSTANCE.getPlayer(player);
        Objective health = uhcPlayer.getScoreboard().getObjective("Health");
        Bukkit.broadcastMessage(String.valueOf(player.getHealth()));
        Bukkit.broadcastMessage(String.valueOf(health));
        if (health != null) {
            health.getScore(uhcPlayer.getName()).setScore((int) player.getHealth());
        }
    }
}
