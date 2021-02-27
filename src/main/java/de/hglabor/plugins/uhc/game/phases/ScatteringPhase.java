package de.hglabor.plugins.uhc.game.phases;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.mechanics.border.Corner;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.plugins.uhc.game.scenarios.Teams;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import de.hglabor.plugins.uhc.util.SpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicInteger;

public class ScatteringPhase extends GamePhase {
    private final BossBar loadBar;
    private int maxPlayers;

    protected ScatteringPhase() {
        super(0, PhaseType.SCATTERING);
        this.loadBar = Bukkit.createBossBar("Scattering", BarColor.GREEN, BarStyle.SOLID);
    }

    @Override
    protected void init() {
        UHCConfig.setPvPWorldSettings(Bukkit.getWorld("world"));
        playerList.getLobbyPlayers().forEach(uhcPlayer -> uhcPlayer.setStatus(UserStatus.SCATTERING));
        maxPlayers = playerList.getScatteringPlayers().size();
        Bukkit.getOnlinePlayers().forEach(loadBar::addPlayer);
        if (Teams.INSTANCE.isEnabled()) {

        } else {
            setSpawnLocations();
            teleportPlayersRecursively(getRandomScatteringPlayer());
        }
    }

    private void teleportPlayersRecursively(UHCPlayer uhcPlayer) {
        uhcPlayer.getBukkitPlayer().ifPresentOrElse(player -> {
            player.teleportAsync(uhcPlayer.getSpawnLocation()).thenAccept(bool -> {
                if (bool != null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1000));
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
            teleportPlayersRecursively(getRandomScatteringPlayer());
        } else {
            Bukkit.getScheduler().runTask(plugin, this::startNextPhase);
        }
    }

    private UHCPlayer getRandomScatteringPlayer() {
        return playerList.getScatteringPlayers().stream().findAny().get();
    }

    private void setSpawnLocations() {
        AtomicInteger counter = new AtomicInteger(1);
        for (UHCPlayer player : playerList.getScatteringPlayers()) {
            if (counter.get() > 4) counter.set(1);
            Corner corner = Corner.getCorner(counter.getAndIncrement());
            player.setSpawnLocation(SpawnUtils.getCornerSpawn(corner, GameManager.INSTANCE.getBorder().getBorderSize()));
        }
    }

    @Override
    protected void tick(int timer) {
        GameManager.INSTANCE.resetTimer();
        ScatteringPhase.this.loadBar.setProgress((double) playerList.getAlivePlayers().size() / maxPlayers);
    }

    @Override
    public String getTimeString(int timer) {
        return "Loading: " + playerList.getAlivePlayers().size() / maxPlayers;
    }

    @Override
    protected GamePhase getNextPhase() {
        return new FarmPhase();
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
