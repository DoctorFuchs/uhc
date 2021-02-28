package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Fireless extends Scenario {
    public final static Fireless INSTANCE = new Fireless();

    private Fireless() {
        super("Fireless", new ItemBuilder(Material.LAVA_BUCKET).build());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (isEnabled()) {
            if (event.getEntity() instanceof Player) {
                final EntityDamageEvent.DamageCause cause = event.getCause();
                switch (cause) {
                    case FIRE:
                    case FIRE_TICK:
                    case LAVA:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageByBlock(EntityDamageByBlockEvent event) {
        if (isEnabled()) {
            if (event.getDamager() == null || event.getDamager().getType().equals(Material.AIR)) {
                return;
            }
            if (event.getDamager().getType() == Material.LAVA) {
                event.setCancelled(true);
            }
        }
    }
}