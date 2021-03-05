package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public class Shieldless extends Scenario {
    public final static Shieldless INSTANCE = new Shieldless();

    private Shieldless() {
        super("Shieldless", new ItemBuilder(Material.SHIELD).build());
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (isEnabled()) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            Material itemType = event.getCurrentItem().getType();
            if (itemType == Material.SHIELD) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (isEnabled()) {
            Material itemType = event.getEntity().getItemStack().getType();
            if (itemType == Material.SHIELD) {
                event.setCancelled(true);
            }
        }
    }
}