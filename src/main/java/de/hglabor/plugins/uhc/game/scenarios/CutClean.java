package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CutClean extends Scenario {
    public final static CutClean INSTANCE = new CutClean();

    private CutClean() {
        super("CutClean", new ItemBuilder(Material.IRON_INGOT).build());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isEnabled()) {
            return;
        }
        Block block = event.getBlock();
        for (CutCleanItems item : CutCleanItems.values()) {
            if (item.origin.equals(block.getType())) {
                event.setDropItems(false);
                ItemStack replacement = new ItemStack(item.replacement);
                Location location = block.getLocation().add(0.5, 0, 0.5);
                block.getWorld().dropItem(location, replacement);
                ExperienceOrb orb = (ExperienceOrb) block.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
                orb.setExperience(item.xpAmount);
                return;
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!isEnabled()) {
            return;
        }
        List<ItemStack> toDelete = new ArrayList<>();
        for (ItemStack drop : event.getDrops()) {
            for (CutCleanItems ccItem : CutCleanItems.values()) {
                if (ccItem.origin.equals(drop.getType())) {
                    toDelete.add(drop);
                }
            }
        }
        for (ItemStack itemStack : toDelete) {
            for (CutCleanItems ccItem : CutCleanItems.values()) {
                if (ccItem.origin.equals(itemStack.getType())) {
                    event.getDrops().add(new ItemStack(ccItem.replacement, itemStack.getAmount()));
                }
            }
        }
        event.getDrops().removeAll(toDelete);
    }

    private enum CutCleanItems {
        // Blocks
        IRON_ORE(Material.IRON_ORE, Material.IRON_INGOT, 2),
        GOLD_ORE(Material.GOLD_ORE, Material.GOLD_INGOT, 3),
        ANCIENT_DEBRIS(Material.ANCIENT_DEBRIS, Material.NETHERITE_SCRAP, 4),
        GRAVEL(Material.GRAVEL, Material.FLINT),
        // Mobs
        PORKCHOP(Material.PORKCHOP, Material.COOKED_PORKCHOP),
        COD(Material.COD, Material.COOKED_COD),
        SALMON(Material.SALMON, Material.COOKED_SALMON),
        BEEF(Material.BEEF, Material.COOKED_BEEF),
        CHICKEN(Material.CHICKEN, Material.COOKED_CHICKEN),
        RABBIT(Material.RABBIT, Material.COOKED_RABBIT),
        MUTTON(Material.MUTTON, Material.COOKED_MUTTON);

        private final Material replacement;
        private final Material origin;
        private int xpAmount;

        CutCleanItems(Material origin, Material replacement, int xpAmount) {
            this.origin = origin;
            this.replacement = replacement;
            this.xpAmount = xpAmount;
        }

        CutCleanItems(Material origin, Material replacement) {
            this.origin = origin;
            this.replacement = replacement;
        }

    }
}
