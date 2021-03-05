package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class HealingKill extends Scenario {
    public final static HealingKill INSTANCE = new HealingKill();

    private HealingKill() {
        super("Healing Kill", new ItemBuilder(Material.GOLDEN_APPLE).build());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (isEnabled()) {
            Player player = event.getEntity();
            if (player.getKiller() != null) {
                Player killer = player.getKiller();
                killer.setHealth(killer.getHealthScale());
            }
        }
    }
}