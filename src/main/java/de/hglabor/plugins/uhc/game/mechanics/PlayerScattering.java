package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.mechanics.border.Corner;
import de.hglabor.plugins.uhc.game.mechanics.chat.GlobalChat;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import de.hglabor.plugins.uhc.util.SpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerScattering extends BukkitRunnable {
    private final BossBar loadBar;
    private final List<UHCPlayer> toTeleport;
    private final int playerAmount;
    private final AtomicInteger playerCounter;
    private final AtomicInteger cornerCounter;
    private final int amountToTeleportEachRun;

    public PlayerScattering(List<UHCPlayer> toTeleport, int amountToTeleportEachRun) {
        this.toTeleport = toTeleport;
        this.playerAmount = toTeleport.size();
        this.amountToTeleportEachRun = amountToTeleportEachRun;
        this.playerCounter = new AtomicInteger();
        this.cornerCounter = new AtomicInteger(1);
        this.loadBar = Bukkit.createBossBar(ChatColor.BOLD + "Scattering | Don't logout", BarColor.GREEN, BarStyle.SOLID);
        init();
    }

    private void init() {
        loadBar.setProgress(0);
        Bukkit.getOnlinePlayers().forEach(loadBar::addPlayer);
    }

    @Override
    public void run() {
        if (toTeleport.isEmpty()) {
            loadBar.removeAll();
            GameManager.INSTANCE.getPhase().startNextPhase();
            cancel();
            return;
        }

        int counter = 0;
        List<UHCPlayer> teleportedPlayers = new ArrayList<>();

        for (UHCPlayer uhcPlayer : toTeleport) {
            if (counter >= amountToTeleportEachRun) {
                break;
            }
            uhcPlayer.setSpawnLocation(getSpawnLocation());
            uhcPlayer.getBukkitPlayer().ifPresent(player -> {
                player.teleport(uhcPlayer.getSpawnLocation());
                player.setGameMode(GameMode.SURVIVAL);
                uhcPlayer.setStatus(UserStatus.INGAME);
            });
            teleportedPlayers.add(uhcPlayer);
            counter++;
            Bukkit.broadcastMessage(GlobalChat.getPrefix() + GlobalChat.hexColor("#F45959") + "Es wurden " + playerCounter.incrementAndGet() + " von " + playerAmount + " teleportiert");
        }
        toTeleport.removeAll(teleportedPlayers);

        loadBar.setProgress((double) playerCounter.get() / playerAmount);
    }

    private Location getSpawnLocation() {
        if (cornerCounter.get() > 4) cornerCounter.set(1);
        return SpawnUtils.getCornerSpawn(Corner.getCorner(cornerCounter.getAndIncrement()), GameManager.INSTANCE.getBorder().getBorderSize());
    }
}
