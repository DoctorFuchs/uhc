package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class ColdWeapons extends Scenario {
    public final static ColdWeapons INSTANCE = new ColdWeapons();

    private ColdWeapons() {
        super("Cold Weapons", new ItemBuilder(Material.BLUE_ICE).build());
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if (isEnabled()) {
            event.getEnchantsToAdd().remove(Enchantment.ARROW_FIRE);
            event.getEnchantsToAdd().remove(Enchantment.FIRE_ASPECT);
        }
    }
}
