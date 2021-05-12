package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.plugins.uhc.game.mechanics.GoldenHead;
import de.hglabor.plugins.uhc.game.mechanics.chat.GlobalChat;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Timebomb extends Scenario {
    public final static Timebomb INSTANCE = new Timebomb();

    private Timebomb() {
        super("Timebomb", new ItemBuilder(Material.CHEST).build());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (isEnabled()) {
            Player player = event.getEntity();
            ArrayList<ItemStack> items = new ArrayList<>(event.getDrops());
            items.add(GoldenHead.INSTANCE.getHeadItem());
            event.getDrops().clear();
            TBThread thread = new TBThread(player.getLocation().getBlock().getLocation(), items, 45);
            thread.placeAndFillChest();
            thread.runTaskTimer(Uhc.getPlugin(), 20, 20);
        }
    }

    public static class TBThread extends BukkitRunnable {
        private final List<ItemStack> itemDrops;
        private Location location;
        private int timeLeft;
        private ArmorStand armorStand;
        private Block leftBlock;
        private Block rightBlock;

        public TBThread(Location location, ArrayList<ItemStack> itemDrops, int timeLeft) {
            this.location = location;
            this.timeLeft = timeLeft;
            this.itemDrops = itemDrops;
        }

        private void placeAndFillChest() {
            leftBlock = location.getBlock();
            rightBlock = location.add(-1, 0, 0).getBlock();

            leftBlock.setType(Material.CHEST);
            rightBlock.setType(Material.CHEST);
            Chest firstChest = (Chest) leftBlock.getState();
            Chest secondChest = (Chest) rightBlock.getState();

            org.bukkit.block.data.type.Chest chestDataLeft = (org.bukkit.block.data.type.Chest) leftBlock.getBlockData();
            org.bukkit.block.data.type.Chest chestDataRight = (org.bukkit.block.data.type.Chest) rightBlock.getBlockData();
            chestDataLeft.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
            chestDataRight.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
            leftBlock.setBlockData(chestDataLeft, false);
            rightBlock.setBlockData(chestDataRight, false);

            Inventory firstChestInventory = firstChest.getInventory();
            Inventory secondChestInventory = secondChest.getInventory();

            AtomicInteger count = new AtomicInteger();
            itemDrops.forEach(item -> {
                if (count.get() < 27)
                    firstChestInventory.addItem(item);
                else
                    secondChestInventory.addItem(item);
                count.getAndIncrement();
            });

            this.location = leftBlock.getLocation().add(0, -1.3, 0.5);
            summonArmorStand(location);
        }

        private void summonArmorStand(Location location) {
            armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setCustomName(String.format(GlobalChat.hexColor("#EC2828") + "%ds", timeLeft));
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
        }

        @Override
        public void run() {
            if (timeLeft > 0) {
                armorStand.setCustomName(String.format(GlobalChat.hexColor("#EC2828") + "%ds", timeLeft));
                timeLeft--;
            } else {
                leftBlock.setType(Material.AIR);
                rightBlock.setType(Material.AIR);
                armorStand.remove();
                leftBlock.getWorld().createExplosion(rightBlock.getLocation(), 10.0f, false, true);
                cancel();
            }
        }
    }
}
