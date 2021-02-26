package de.hglabor.plugins.uhc.game.phases;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.mechanics.Corner;
import de.hglabor.plugins.uhc.game.scenarios.Teams;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import de.hglabor.plugins.uhc.util.SpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class ScatteringPhase extends GamePhase {
    private BossBar loadBar;
    private int maxPlayers;

    protected ScatteringPhase() {
        super(0);
        this.loadBar = Bukkit.createBossBar("Scattering", BarColor.GREEN, BarStyle.SOLID);
    }

    @Override
    protected void init() {
        playerList.getLobbyPlayers().forEach(uhcPlayer -> uhcPlayer.setStatus(UserStatus.SCATTERING));
        maxPlayers = playerList.getScatteringPlayers().size();
        Bukkit.getOnlinePlayers().forEach(player -> loadBar.addPlayer(player));
        if (Teams.INSTANCE.isEnabled()) {

        } else {
            setSpawnLocations();
            teleportPlayersRecursively(getRandomScatteringPlayer());
        }
    }

    private void teleportPlayersRecursively(UHCPlayer uhcPlayer) {
        uhcPlayer.getPlayer().ifPresentOrElse(player -> {
            player.teleportAsync(uhcPlayer.getSpawnLocation()).thenAccept(bool -> {
                if (bool != null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1000));
                    uhcPlayer.setStatus(UserStatus.INGAME);
                }
                if (playerList.getScatteringPlayers().size() > 0) {
                    teleportPlayersRecursively(getRandomScatteringPlayer());
                }
            });
        }, () -> {
            playerList.remove(uhcPlayer.getUuid());
            if (playerList.getScatteringPlayers().size() > 0) {
                teleportPlayersRecursively(getRandomScatteringPlayer());
            }
        });
    }

    @NotNull
    private UHCPlayer getRandomScatteringPlayer() {
        return playerList.getScatteringPlayers().stream().findAny().get();
    }

    private void setSpawnLocations() {
        AtomicInteger counter = new AtomicInteger(1);
        for (UHCPlayer player : playerList.getScatteringPlayers()) {
            if (counter.get() > 4) counter.set(1);
            Corner corner = Corner.getCorner(counter.getAndIncrement());
            player.setSpawnLocation(SpawnUtils.getCornerSpawn(corner, GameManager.INSTANCE.getBorder()));
        }
    }

    @Override
    protected void tick(int timer) {
        ScatteringPhase.this.loadBar.setProgress((double) playerList.getAlivePlayers().size() / maxPlayers);
    }

    @Override
    public PhaseType getType() {
        return PhaseType.SCATTERING;
    }

    @Override
    protected String getTimeString(int timer) {
        return null;
    }

    @Override
    protected GamePhase getNextPhase() {
        return new FarmPhase();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        UHCPlayer player = playerList.getPlayer(event.getPlayer());
        if (player.isAlive()) {
            event.setCancelled(true);
        }
    }
}
