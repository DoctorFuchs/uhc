package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class RodKnockback extends Scenario {
    public final static RodKnockback INSTANCE = new RodKnockback();

    private RodKnockback() {
        super("Rod Knockback", new ItemBuilder(Material.FISHING_ROD).build());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (isEnabled()) {
            if (event.getEntity() instanceof FishHook) {
                if (event.getEntity().getShooter() instanceof Player) {
                    if (event.getHitEntity() == null) { return; }
                    Projectile hook = event.getEntity();
                    Player hookShooter = (Player) hook.getShooter();
                    LivingEntity hitEntity = (LivingEntity) event.getHitEntity();
                    double velX = hook.getLocation().getDirection().getX() / 1.8;
                    velX -= velX * 2.0;
                    double velY = 0.452;
                    double velZ = hook.getLocation().getDirection().getZ() / 1.8;
                    hitEntity.damage(0.000001, hookShooter);
                    hitEntity.setVelocity(new Vector(velX, velY, velZ));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (isEnabled()) {
            if (event.getHook().getShooter() instanceof Player) {
                FishHook hook = event.getHook();
                Player hookShooter = (Player) hook.getShooter();
                if (event.getCaught() == null) { return; }
                if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                    event.setCancelled(true);
                    hook.remove();
                }
            }
        }
    }
}
