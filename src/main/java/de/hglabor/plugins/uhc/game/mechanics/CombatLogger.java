package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftZombie;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public final class CombatLogger implements Listener {
    public final static CombatLogger INSTANCE = new CombatLogger();
    private final Map<UUID, Inventory> inventories;
    private final OfflineTimer offlineTimer;

    private CombatLogger() {
        this.inventories = new HashMap<>();
        this.offlineTimer = new OfflineTimer();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = PlayerList.INSTANCE.getPlayer(player);
        if (uhcPlayer.getStatus().equals(UserStatus.OFFLINE)) {
            spawnCombatLogZombie(player, uhcPlayer);
            offlineTimer.putAndStartTimer(uhcPlayer);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = PlayerList.INSTANCE.getPlayer(player);
        if (uhcPlayer.getStatus().equals(UserStatus.INGAME)) {
            if (uhcPlayer.getCombatLogMob() == null) return;
            offlineTimer.stopTimer(uhcPlayer);
            Optional.ofNullable(Bukkit.getEntity(uhcPlayer.getUuid())).ifPresent(entity -> {
                inventories.remove(entity.getUniqueId());
                entity.removeMetadata(player.getUniqueId().toString(), Uhc.getPlugin());
                entity.remove();
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            for (UUID uuid : inventories.keySet()) {
                if (zombie.hasMetadata(uuid.toString())) {
                    modifyZombieDrop(event, zombie);
                    PlayerList.INSTANCE.getPlayer(uuid).ifPresent(uhcPlayer -> {
                        uhcPlayer.setStatus(UserStatus.ELIMINATED);
                        uhcPlayer.setCombatLogMob(null);
                        uhcPlayer.getBukkitPlayer().ifPresent(player -> player.kickPlayer("Your combatlogger died"));
                    });
                    return;
                }
            }
        }
    }

    private void modifyZombieDrop(EntityDeathEvent event, Zombie zombie) {
        for (ItemStack drop : event.getDrops()) {
            drop.setAmount(0);
            drop.setType(Material.AIR);
        }
        Inventory inventory = inventories.get(zombie.getUniqueId());
        inventories.remove(zombie.getUniqueId());
        event.getDrops().addAll(Arrays.asList(inventory.getContents()));
    }

    private void spawnCombatLogZombie(Player player, UHCPlayer uhcPlayer) {
        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        uhcPlayer.setCombatLogMob(zombie.getUniqueId());
        inventories.put(zombie.getUniqueId(), player.getInventory());
        zombie.setMetadata(player.getUniqueId().toString(), new FixedMetadataValue(Uhc.getPlugin(), ""));
        zombie.setAI(false);
        zombie.setCustomName(player.getName());
        zombie.setCustomNameVisible(true);
        zombie.setCanPickupItems(false);
        zombie.setRemoveWhenFarAway(false);
        zombie.setShouldBurnInDay(false);
        setItem(zombie, player.getInventory().getHelmet(), EnumItemSlot.HEAD);
        setItem(zombie, player.getInventory().getChestplate(), EnumItemSlot.CHEST);
        setItem(zombie, player.getInventory().getLeggings(), EnumItemSlot.LEGS);
        setItem(zombie, player.getInventory().getBoots(), EnumItemSlot.FEET);
        setItem(zombie, player.getInventory().getItemInMainHand(), EnumItemSlot.MAINHAND);
        setItem(zombie, player.getInventory().getItemInOffHand(), EnumItemSlot.OFFHAND);
    }

    private void setItem(Zombie zombie, ItemStack itemStack, EnumItemSlot slot) {
        if (itemStack != null) {
            ((CraftZombie) zombie).getHandle().setSlot(slot, CraftItemStack.asNMSCopy(itemStack));
        }
    }
}
