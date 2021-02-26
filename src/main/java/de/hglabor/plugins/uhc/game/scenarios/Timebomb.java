package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.ChatColor;
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
            event.getDrops().clear();
            TBThread thread = new TBThread(player.getLocation(), items);
            thread.placeAndFillChest();
        }
    }

    public class TBThread {
        private Location location;
        private ArrayList<ItemStack> itemDrops;
        private int timeLeft = 45;
        private ArmorStand armorStand;
        private Block firstBlock;
        private Block secondBlock;

        public TBThread(Location location, ArrayList<ItemStack> itemDrops) {
            this.location = location;
            this.itemDrops = itemDrops;
        }

        private void placeAndFillChest() {
            firstBlock = location.getBlock();
            secondBlock = location.add(1, 0, 0).getBlock();
            firstBlock.setType(Material.CHEST);
            secondBlock.setType(Material.CHEST);
            Chest firstChest = (Chest) firstBlock.getState();

            Inventory chestInventory = firstChest.getInventory();
            itemDrops.forEach(chestInventory::addItem);

            summonArmorStand(location);
        }

        private void summonArmorStand(Location location) {
            armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
            startRunnable();
        }

        private void startRunnable() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (timeLeft > 0) {
                        armorStand.setCustomName(String.format(ChatColor.AQUA + "%d" + ChatColor.BLUE + "s", timeLeft));
                        timeLeft--;
                    } else {
                        firstBlock.getWorld().createExplosion(firstBlock.getLocation(), 10.0f, false, true);
                        firstBlock.setType(Material.AIR);
                        secondBlock.setType(Material.AIR);
                        armorStand.remove();
                    }
                }
            }.runTaskTimer(Uhc.getPlugin(), 20, 45);
        }
    }
}