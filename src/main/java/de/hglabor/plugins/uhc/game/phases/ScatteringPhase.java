package de.hglabor.plugins.uhc.game.phases;

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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class ScatteringPhase extends GamePhase {
    private BossBar loadBar;

    protected ScatteringPhase() {
        super(0);
        this.loadBar = Bukkit.createBossBar("Scattering", BarColor.GREEN, BarStyle.SOLID);
    }

    @Override
    protected void init() {
        playerList.getLobbyPlayers().forEach(uhcPlayer -> uhcPlayer.setStatus(UserStatus.SCATTERING));
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

    }

    @Override
    public PhaseType getType() {
        return null;
    }

    @Override
    protected String getTimeString(int timer) {
        return null;
    }

    @Override
    protected GamePhase getNextPhase() {
        return null;
    }
}
