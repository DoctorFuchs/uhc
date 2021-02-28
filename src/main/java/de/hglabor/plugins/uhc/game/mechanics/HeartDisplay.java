package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;

public class HeartDisplay implements Listener {
    public static final HeartDisplay INSTANCE = new HeartDisplay();

    private HeartDisplay() {
    }

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

    public void enableHealthBar(Player player) {
        UHCPlayer uhcPlayer = PlayerList.INSTANCE.getPlayer(player);
        Objective objective = uhcPlayer.getScoreboard().registerNewObjective("health", "health", ChatColor.RED + "❤", RenderType.HEARTS);
        objective.getScore(uhcPlayer.getName()).setScore((int) player.getHealth());
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    public void updateHeal(Player player) {
        UHCPlayer uhcPlayer = PlayerList.INSTANCE.getPlayer(player);
        Objective health = uhcPlayer.getScoreboard().getObjective("health");
        if (health != null) {
            health.getScore(uhcPlayer.getName()).setScore((int) player.getHealth());
        }
    }
}
