package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Diamondless extends Scenario {
    public final static Diamondless INSTANCE = new Diamondless();

    private Diamondless() {
        super("Diamondless", new ItemBuilder(Material.DIAMOND).build());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isEnabled()) {
            Block block = event.getBlock();
            if (block.getType() == Material.DIAMOND_ORE) {
                event.getBlock().getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (isEnabled()) {
            event.getDrops().add(new ItemStack(Material.DIAMOND, 2));
        }
    }
}