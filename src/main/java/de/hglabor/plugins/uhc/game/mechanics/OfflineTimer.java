package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.plugins.uhc.player.UserStatus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class OfflineTimer {
    private final Map<UUID, BukkitTask> offlinePlayers;

    public OfflineTimer() {
        this.offlinePlayers = new HashMap<>();
    }

    public void putAndStartTimer(final UHCPlayer uhcPlayer) {
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (uhcPlayer.getStatus().equals(UserStatus.ELIMINATED)) {
                    eliminate(uhcPlayer);
                    cancel();
                }
                if (uhcPlayer.getOfflineTime().getAndDecrement() <= 0 && !isCancelled()) {
                    eliminate(uhcPlayer);
                    cancel();
                }
            }
        }.runTaskTimer(Uhc.getPlugin(), 0, 20L);
        offlinePlayers.put(uhcPlayer.getUuid(), bukkitTask);
    }

    private void eliminate(UHCPlayer uhcPlayer) {
        uhcPlayer.setStatus(UserStatus.ELIMINATED);
        offlinePlayers.remove(uhcPlayer.getUuid());
    }

    public void stopTimer(final UHCPlayer uhcPlayer) {
        Optional.ofNullable(offlinePlayers.get(uhcPlayer.getUuid())).ifPresent(BukkitTask::cancel);
        offlinePlayers.remove(uhcPlayer.getUuid());
    }
}
