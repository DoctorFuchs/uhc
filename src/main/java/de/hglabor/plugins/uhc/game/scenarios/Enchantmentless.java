package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Enchantmentless extends Scenario {
    public final static Enchantmentless INSTANCE = new Enchantmentless();

    private Enchantmentless() {
        super("Enchantmentless", new ItemBuilder(Material.ENCHANTING_TABLE).build());
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (isEnabled()) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            Material itemType = event.getCurrentItem().getType();
            if (itemType == Material.ENCHANTING_TABLE || itemType == Material.ANVIL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (isEnabled()) {
            if (event.getClickedBlock() == null || event.getClickedBlock().getType().equals(Material.AIR)) {
                return;
            }
            Material blockType = event.getClickedBlock().getType();
            if (blockType == Material.ENCHANTING_TABLE || blockType == Material.ANVIL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if (isEnabled()) {
            event.setCancelled(true);
        }
    }
}
