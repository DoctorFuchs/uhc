package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class HasteyBoys extends Scenario {
    public final static HasteyBoys INSTANCE = new HasteyBoys();

    private HasteyBoys() {
        super("Hastey Boys", new ItemBuilder(Material.NETHERITE_PICKAXE).build());
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (isEnabled()) {
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType().equals(Material.AIR)) {
                return;
            }
            ArrayList<String> typeEndings = new ArrayList<>(Arrays.asList("_AXE", "_PICKAXE", "_SHOVEL"));
            for (String ending : typeEndings) {
                if (item.getType().name().contains(ending)) {
                    item.addEnchantment(Enchantment.DIG_SPEED, 3);
                    item.addEnchantment(Enchantment.DURABILITY, 1);
                }
            }
        }
    }
}