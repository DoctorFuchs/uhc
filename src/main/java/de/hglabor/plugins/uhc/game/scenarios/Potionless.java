package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;

public class Potionless extends Scenario {
    public final static Potionless INSTANCE = new Potionless();

    private Potionless() {
        super("Shieldless", new ItemBuilder(Material.POTION).build());
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        if (isEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBrewingStandFuel(BrewingStandFuelEvent event) {
        if (isEnabled()) {
            event.setCancelled(true);
        }
    }
}