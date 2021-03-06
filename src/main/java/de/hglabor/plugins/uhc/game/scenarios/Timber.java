package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class Timber extends Scenario {
    public final static Timber INSTANCE = new Timber();

    private Timber() {
        super("Timber", new ItemBuilder(Material.WOODEN_AXE).build());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isEnabled()) {
            return;
        }
        Block block = event.getBlock();
        String blockTypeName = block.getType().name().toLowerCase();
        if (blockTypeName.contains("wood") || blockTypeName.contains("log")) {
            breakSurroundingWood(block, 0);
        }
    }

    public void breakSurroundingWood(Block block, int amount) {
        String blockTypeName = block.getType().name().toLowerCase();

        if (amount > 12) {
            return;
        }

        if (blockTypeName.contains("wood") || blockTypeName.contains("log")) {
            block.breakNaturally();

            BlockFace[] faces = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (BlockFace face : faces) {
                breakSurroundingWood(block.getRelative(face), amount++);
            }
        }
    }
}
