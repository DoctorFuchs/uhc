package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CutClean extends Scenario {
    public final static CutClean INSTANCE = new CutClean();

    private CutClean() {
        super("CutClean", new ItemBuilder(Material.IRON_INGOT).build());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isEnabled()) {
            Block block = event.getBlock();
            for (CutCleanItems item : CutCleanItems.values()) {
                if (item.name().equalsIgnoreCase(block.getType().name())) {
                    event.setDropItems(false);
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(item.replacement));
                    ExperienceOrb orb = (ExperienceOrb) block.getLocation().getWorld().spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB);
                    orb.setExperience(item.xpAmount);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (isEnabled()) {
            List<ItemStack> toDelete = new ArrayList<>();
            for (ItemStack drop : event.getDrops()) {
                for (CutCleanItems ccItem : CutCleanItems.values()) {
                    if (ccItem.name().equalsIgnoreCase(drop.getType().toString())) {
                        toDelete.add(drop);
                    }
                }
            }
            for (ItemStack itemStack : toDelete) {
                for (CutCleanItems ccItem : CutCleanItems.values()) {
                    if (ccItem.name().equalsIgnoreCase(itemStack.getType().toString())) {
                        event.getDrops().add(new ItemStack(ccItem.replacement, itemStack.getAmount()));
                    }
                }
            }
            event.getDrops().removeAll(toDelete);
        }
    }

    private enum CutCleanItems {
        // Blocks
        IRON_ORE(Material.IRON_INGOT, 2),
        GOLD_ORE(Material.GOLD_INGOT, 3),
        ANCIENT_DEBRIS(Material.NETHERITE_SCRAP, 4),
        GRAVEL(Material.FLINT),
        // Mobs
        PORKCHOP(Material.COOKED_PORKCHOP),
        COD(Material.COOKED_COD),
        SALMON(Material.COOKED_SALMON),
        BEEF(Material.COOKED_BEEF),
        CHICKEN(Material.COOKED_CHICKEN),
        RABBIT(Material.COOKED_RABBIT),
        MUTTON(Material.COOKED_MUTTON);

        private final Material replacement;
        private int xpAmount = 0;

        CutCleanItems(Material replacement, int xpAmount) {
            this.replacement = replacement;
            this.xpAmount = xpAmount;
        }

        CutCleanItems(Material replacement) {
            this.replacement = replacement;
        }
    }
}
