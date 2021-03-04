package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.mechanics.border.Corner;
import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import de.hglabor.plugins.uhc.util.SpawnUtils;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerScattering extends BukkitRunnable {
    private final List<UHCPlayer> toTeleport;
    private final int size;
    private final AtomicInteger playerCounter;
    private final AtomicInteger cornerCounter;

    public PlayerScattering(List<UHCPlayer> toTeleport) {
        this.toTeleport = toTeleport;
        this.size = toTeleport.size();
        this.playerCounter = new AtomicInteger();
        this.cornerCounter = new AtomicInteger(1);
    }

    @Override
    public void run() {
        if (playerCounter.get() == size) {
            GameManager.INSTANCE.getPhase().startNextPhase();
            cancel();
            return;
        }

        UHCPlayer toTeleport = this.toTeleport.get(playerCounter.getAndIncrement());
        toTeleport.setSpawnLocation(getSpawnLocation());
        toTeleport.getBukkitPlayer().ifPresentOrElse(player -> {
            player.teleport(toTeleport.getSpawnLocation());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 1000));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 30, 10));
            toTeleport.setStatus(UserStatus.INGAME);
        }, () -> PlayerList.INSTANCE.remove(toTeleport.getUuid()));
    }

    private Location getSpawnLocation() {
        if (cornerCounter.get() > 4) cornerCounter.set(1);
        return SpawnUtils.getCornerSpawn(Corner.getCorner(cornerCounter.getAndIncrement()), GameManager.INSTANCE.getBorder().getBorderSize());
    }
}
