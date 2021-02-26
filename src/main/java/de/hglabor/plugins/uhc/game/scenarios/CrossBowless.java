package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public class CrossBowless extends Scenario {
    public final static CrossBowless INSTANCE = new CrossBowless();

    private CrossBowless() {
        super("Cross-Bowless", new ItemBuilder(Material.CROSSBOW).build());
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (isEnabled()) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            Material itemType = event.getCurrentItem().getType();
            if (itemType == Material.BOW || itemType == Material.CROSSBOW || itemType == Material.ARROW) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (isEnabled()) {
            Material itemType = event.getEntity().getItemStack().getType();
            if (itemType == Material.BOW || itemType == Material.CROSSBOW || itemType == Material.ARROW) {
                event.setCancelled(true);
            }
        }
    }
}