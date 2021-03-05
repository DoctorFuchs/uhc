package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantedDeath extends Scenario {
    public final static EnchantedDeath INSTANCE = new EnchantedDeath();

    private EnchantedDeath() {
        super("Enchanted Death", new ItemBuilder(Material.ENCHANTED_BOOK).build());
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if (isEnabled()) {
            event.getEnchanter().damage(2);
        }
    }
}
