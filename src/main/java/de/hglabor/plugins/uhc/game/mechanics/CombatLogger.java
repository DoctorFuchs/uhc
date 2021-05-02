package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.mechanics.events.PlayerKilledPlayerEvent;
import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
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
    private final DeathMessenger deathMessenger;

    private CombatLogger() {
        this.inventories = new HashMap<>();
        this.offlineTimer = new OfflineTimer();
        this.deathMessenger = new DeathMessenger();
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
            Optional.ofNullable(Bukkit.getEntity(uhcPlayer.getCombatLogMob())).ifPresent(entity -> {
                inventories.remove(entity.getUniqueId());
                entity.removeMetadata(player.getUniqueId().toString(), Uhc.getPlugin());
                entity.remove();
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Zombie)) {
            return;
        }
        Zombie zombie = (Zombie) event.getEntity();
        for (UUID uuid : inventories.keySet()) {
            if (zombie.hasMetadata(uuid.toString())) {
                modifyZombieDrop(event, uuid);
                PlayerList.INSTANCE.getPlayer(uuid).ifPresent(uhcPlayer -> {
                    uhcPlayer.setStatus(UserStatus.ELIMINATED);
                    uhcPlayer.setCombatLogMob(null);
                    uhcPlayer.getBukkitPlayer().ifPresent(player -> player.kickPlayer(ChatColor.RED + "Your combatlogger died"));
                    if (zombie.getKiller() != null) {
                        Bukkit.getPluginManager().callEvent(new PlayerKilledPlayerEvent(zombie.getKiller(), zombie, uuid));
                    }
                });
                zombie.remove();
                return;
            }
        }
    }

    private void modifyZombieDrop(EntityDeathEvent event, UUID uuid) {
        for (ItemStack drop : event.getDrops()) {
            drop.setAmount(0);
            drop.setType(Material.AIR);
        }
        Inventory inventory = inventories.get(uuid);
        inventories.remove(uuid);
        event.getDrops().addAll(Arrays.asList(inventory.getContents()));
    }

    private void spawnCombatLogZombie(Player player, UHCPlayer uhcPlayer) {
        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        uhcPlayer.setCombatLogMob(zombie.getUniqueId());
        inventories.put(player.getUniqueId(), player.getInventory());
        zombie.setMetadata(player.getUniqueId().toString(), new FixedMetadataValue(Uhc.getPlugin(), ""));
        zombie.setCustomName(player.getName());
        zombie.setCustomNameVisible(true);
        zombie.setCanPickupItems(false);
        zombie.setAI(false);
        zombie.setRemoveWhenFarAway(false);
        zombie.setShouldBurnInDay(false);
        setItem(zombie, player.getInventory().getHelmet(), EnumItemSlot.HEAD);
        setItem(zombie, player.getInventory().getChestplate(), EnumItemSlot.CHEST);
        setItem(zombie, player.getInventory().getLeggings(), EnumItemSlot.LEGS);
        setItem(zombie, player.getInventory().getBoots(), EnumItemSlot.FEET);
        setItem(zombie, player.getInventory().getItemInMainHand(), EnumItemSlot.MAINHAND);
        setItem(zombie, player.getInventory().getItemInOffHand(), EnumItemSlot.OFFHAND);
    }

    public void placeHead(Location location, UUID player) {
        Block headBlock = location.clone().add(0, 1, 0).getBlock();
        Block footBlock = location.clone().getBlock();
        headBlock.setType(Material.PLAYER_HEAD);
        BlockState state = headBlock.getState();
        Skull head = (Skull) state;
        head.setOwningPlayer(Bukkit.getOfflinePlayer(player));
        head.update();
        footBlock.setType(Material.WARPED_FENCE);
    }

    private void setItem(Zombie zombie, ItemStack itemStack, EnumItemSlot slot) {
        if (itemStack != null) {
            ((CraftZombie) zombie).getHandle().setSlot(slot, CraftItemStack.asNMSCopy(itemStack));
        }
    }
}
