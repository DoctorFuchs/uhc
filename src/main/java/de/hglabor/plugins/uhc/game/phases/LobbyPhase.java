package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.config.CKeys;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class LobbyPhase extends GamePhase {
    private final World lobby;

    public LobbyPhase() {
        super(UHCConfig.getInteger(CKeys.LOBBY_START_TIME), PhaseType.LOBBY);
        this.lobby = Bukkit.getWorld("lobby");
    }

    @Override
    protected void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        UHCConfig.setLobbySettings(lobby);
    }

    @Override
    protected void tick(int timer) {
        int timeLeft = maxPhaseTime - timer;
        if (timeLeft == 0) {
            this.startNextPhase();
        }
    }

    @Override
    public void startNextPhase() {
        GameManager.INSTANCE.resetTimer();
        super.startNextPhase();
    }

    @Override
    public int getAlivePlayers() {
        return (int) playerList.getAllPlayers().stream().filter(player -> player.getStatus().equals(UserStatus.LOBBY)).count();
    }

    @Override
    public String getTimeString(int timer) {
        int timeLeft = maxPhaseTime - timer;
        return "Start: " + TimeConverter.stringify(timeLeft);
    }

    @Override
    protected GamePhase getNextPhase() {
        return new ScatteringPhase();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = playerList.getPlayer(player);
        player.teleportAsync(lobby.getSpawnLocation());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        playerList.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
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
