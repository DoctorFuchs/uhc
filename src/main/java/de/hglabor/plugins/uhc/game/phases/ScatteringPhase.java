package de.hglabor.plugins.uhc.game.phases;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.config.CKeys;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.plugins.uhc.game.mechanics.border.Corner;
import de.hglabor.plugins.uhc.game.scenarios.Teams;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import de.hglabor.plugins.uhc.util.SpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicInteger;

public class ScatteringPhase extends GamePhase {
    private final BossBar loadBar;
    private final AtomicInteger counter;
    private int maxPlayers;


    protected ScatteringPhase() {
        super(0, PhaseType.SCATTERING);
        this.loadBar = Bukkit.createBossBar(ChatColor.BOLD + "Scattering | Don't logout", BarColor.GREEN, BarStyle.SOLID);
        this.counter = new AtomicInteger(1);
    }

    @Override
    protected void init() {
        UHCConfig.setPvPWorldSettings(Bukkit.getWorld("world"));
        playerList.getLobbyPlayers().forEach(uhcPlayer -> uhcPlayer.setStatus(UserStatus.SCATTERING));
        maxPlayers = playerList.getScatteringPlayers().size();
        loadBar.setProgress(0);
        Bukkit.getOnlinePlayers().forEach(loadBar::addPlayer);
        if (Teams.INSTANCE.isEnabled()) {

        } else {
            teleportPlayersRecursively(getRandomScatteringPlayer());
        }
    }

    private void teleportPlayersRecursively(UHCPlayer uhcPlayer) {
        uhcPlayer.setSpawnLocation(getSpawnLocation());
        uhcPlayer.getBukkitPlayer().ifPresentOrElse(player -> {
            player.teleportAsync(uhcPlayer.getSpawnLocation()).thenAccept(bool -> {
                if (bool != null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 1000));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 30, 10));
                    uhcPlayer.setStatus(UserStatus.INGAME);
                }
                teleportOrNextPhase();
            });
        }, () -> {
            playerList.remove(uhcPlayer.getUuid());
            teleportOrNextPhase();
        });
    }

    @Override
    public void startNextPhase() {
        loadBar.removeAll();
        super.startNextPhase();
    }

    private void teleportOrNextPhase() {
        if (playerList.getScatteringPlayers().size() > 0) {
            Bukkit.getScheduler().runTaskLater(Uhc.getPlugin(), () -> teleportPlayersRecursively(getRandomScatteringPlayer()), UHCConfig.getInteger(CKeys.SCATTER_TELEPORT_DELAY));
        } else {
            Bukkit.getScheduler().runTask(plugin, this::startNextPhase);
        }
    }

    private UHCPlayer getRandomScatteringPlayer() {
        return playerList.getScatteringPlayers().stream().findAny().get();
    }

    private Location getSpawnLocation() {
        if (counter.get() > 4) counter.set(1);
        return SpawnUtils.getCornerSpawn(Corner.getCorner(counter.getAndIncrement()), GameManager.INSTANCE.getBorder().getBorderSize());
    }

    @Override
    protected void tick(int timer) {
        GameManager.INSTANCE.resetTimer();
        ScatteringPhase.this.loadBar.setProgress((double) playerList.getAlivePlayers().size() / maxPlayers);
    }

    @Override
    public String getTimeString(int timer) {
        return ChatColor.AQUA + "Loading: " + ChatColor.GREEN + +playerList.getAlivePlayers().size() / maxPlayers;
    }

    @Override
    protected GamePhase getNextPhase() {
        return new FarmPhase();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().kickPlayer(ChatColor.RED + "You can't log in during scattering");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        playerList.remove(player.getUniqueId());
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(!player.hasPermission("group.mod") && !player.isOp());
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        UHCPlayer player = playerList.getPlayer(event.getPlayer());
        if (player.isAlive()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onVehicleDamage(VehicleDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onHangingBreak(HangingBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
