package de.hglabor.plugins.uhc.game.phases;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.config.CKeys;
import de.hglabor.plugins.uhc.config.UHCConfig;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.mechanics.GlobalChat;
import de.hglabor.plugins.uhc.game.mechanics.PlayerScattering;
import de.hglabor.plugins.uhc.game.scenarios.Teams;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
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

public class ScatteringPhase extends GamePhase {
    private final World world;
    private int maxPlayers;

    protected ScatteringPhase() {
        super(0, PhaseType.SCATTERING);
        this.world = Bukkit.getWorld("world");
    }

    @Override
    protected void init() {
        GlobalChat.INSTANCE.enable(false);
       // MobRemover.INSTANCE.enable(world);
        UHCConfig.setPvPWorldSettings(world);
        playerList.getLobbyPlayers().forEach(uhcPlayer -> uhcPlayer.setStatus(UserStatus.SCATTERING));
        maxPlayers = playerList.getScatteringPlayers().size();
        if (Teams.INSTANCE.isEnabled()) {

        } else {
            PlayerScattering playerScattering = new PlayerScattering(playerList.getScatteringPlayers(), 4);
            playerScattering.runTaskTimer(Uhc.getPlugin(),0,UHCConfig.getInteger(CKeys.SCATTER_TELEPORT_DELAY));
        }
    }

    @Override
    protected void tick(int timer) {
        GameManager.INSTANCE.resetTimer();
    }

    @Override
    public String getTimeString(int timer) {
        return ChatColor.AQUA + "Loading: " + ChatColor.GREEN + playerList.getAlivePlayers().size() + "/" + maxPlayers;
    }

    @Override
    protected GamePhase getNextPhase() {
        return new FarmPhase();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().kickPlayer(ChatColor.RED + "You can't log in during scattering");
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        playerList.remove(player.getUniqueId());
    }

    @EventHandler
    private void onPlayerJump(PlayerJumpEvent event) {
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
