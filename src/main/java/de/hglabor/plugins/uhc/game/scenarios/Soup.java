package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Soup extends Scenario {
    public final static Soup INSTANCE = new Soup();

    private Soup() {
        super("Soup", new ItemBuilder(Material.MUSHROOM_STEW).build());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (isEnabled()) {
            if (event.getItem() == null) { return; }
            if (event.getAction() == Action.LEFT_CLICK_AIR) { return; }

            Player player = event.getPlayer();
            if (event.hasItem() && event.getMaterial() == Material.MUSHROOM_STEW) {
                if (event.getHand() == EquipmentSlot.OFF_HAND) { return; }

                if (player.getHealth() < player.getHealthScale()) {
                    player.setHealth(Math.min(player.getHealth() + 4, player.getHealthScale()));
                    player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL));
                } else if (player.getFoodLevel() < 20) {
                    player.setFoodLevel(Math.min(player.getFoodLevel() + 6, 20));
                    player.setSaturation(player.getSaturation() + 4);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL));
                }
            }
        }
    }
}
