package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BloodDiamondsNetherite extends Scenario {
    public final static BloodDiamondsNetherite INSTANCE = new BloodDiamondsNetherite();

    private BloodDiamondsNetherite() {
        super("Blood Diamonds and Netherite", new ItemBuilder(Material.DIAMOND_ORE).build());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isEnabled()) {
            if (event.getBlock().getType() == Material.DIAMOND_ORE) {
                event.getPlayer().damage(1);
            }

            if (event.getBlock().getType() == Material.ANCIENT_DEBRIS) {
                event.getPlayer().damage(2);
            }
        }
    }
}