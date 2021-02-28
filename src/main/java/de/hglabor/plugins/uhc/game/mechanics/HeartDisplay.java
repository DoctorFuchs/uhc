package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
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

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() == null) {
            return;
        }
        if (!(event.getEntity() instanceof AbstractArrow)) {
            return;
        }
        ProjectileSource shooter = event.getEntity().getShooter();
        if (shooter instanceof Player) {
            LivingEntity hitEntity = (LivingEntity) event.getHitEntity();
            ((Player) shooter).sendMessage(ChatColor.AQUA + hitEntity.getName() + " is now at " + ChatColor.GREEN + hitEntity.getHealth() + ChatColor.RED + " ❤");
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
