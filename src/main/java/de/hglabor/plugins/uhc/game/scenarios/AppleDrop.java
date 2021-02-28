package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.plugins.uhc.game.config.CKeys;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.utils.noriskutils.ChanceUtils;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

public class AppleDrop extends Scenario {
    public final static AppleDrop INSTANCE = new AppleDrop();
    private int dropRate;

    private AppleDrop() {
        super("AppleDrop", new ItemBuilder(Material.APPLE).build());
        this.dropRate = 5;
    }

    @Override
    protected void saveToConfig() {
        Uhc plugin = Uhc.getPlugin();
        plugin.getConfig().addDefault(CKeys.SCENARIOS + "." + getName() + "." + "dropRate", dropRate);
        super.saveToConfig();
    }

    @Override
    protected void loadConfig() {
        dropRate = UHCConfig.getInteger(CKeys.SCENARIOS + "." + getName() + "." + "dropRate");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        dropApple(event.getBlock());
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        dropApple(event.getBlock());
    }

    private void dropApple(Block block) {
        if (block.getType().name().toUpperCase().endsWith("_LEAVES")) {
            return;
        }
        if (isEnabled()) {
            if (ChanceUtils.roll(dropRate)) {
                block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }
}
