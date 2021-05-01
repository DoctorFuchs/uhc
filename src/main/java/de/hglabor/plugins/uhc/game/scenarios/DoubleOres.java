package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class DoubleOres extends Scenario {
    public final static DoubleOres INSTANCE = new DoubleOres();

    private DoubleOres() {
        super("Double Ores", new ItemBuilder(Material.DIAMOND_PICKAXE).setAmount(2).build());
    }

    private ArrayList<Material> ores = new ArrayList<>(Arrays.asList(Material.COAL_ORE, Material.IRON_INGOT, Material.GOLD_INGOT,
            Material.LAPIS_ORE, Material.REDSTONE_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.ANCIENT_DEBRIS));

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isEnabled()) {
            return;
        }
        Block block = event.getBlock();
        Location location = block.getLocation();
        if (ores.contains(block.getType())) {
            block.getDrops().forEach(itemStack -> {
                location.getWorld().dropItem(location, itemStack);
            });
        }
    }
}
