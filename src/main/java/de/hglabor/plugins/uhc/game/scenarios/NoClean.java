package de.hglabor.plugins.uhc.game.scenarios;

import com.google.common.collect.ImmutableMap;
import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.localization.Localization;
import de.hglabor.utils.noriskutils.ChatUtils;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class NoClean extends Scenario {
    public final static NoClean INSTANCE = new NoClean();

    private NoClean() {
        super("NoClean", new ItemBuilder(Material.NETHERITE_CHESTPLATE).build());
    }

    static HashMap<Player, NoCleanThread> threads = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (isEnabled()) {
            Player player = event.getEntity();
            if (player.getKiller() != null) {
                Player killer = player.getKiller();
                new NoCleanThread(killer);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (isEnabled()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (threads.containsKey(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (isEnabled()) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                if (threads.containsKey(player)) {
                    damager.sendMessage(Localization.INSTANCE.getMessage("scenario.noclean.timeleft",
                            ImmutableMap.of("numberInSeconds", String.valueOf(threads.get(player).getRemainingTime())),
                            ChatUtils.locale(player)));
                }

                if (threads.containsKey(damager)) {
                    NoCleanThread thread = threads.get(damager);
                    thread.task.cancel();
                    threads.remove(damager);
                }
            }
        }
    }

    public static class NoCleanThread {
        private Player player;
        private long start;
        private BukkitTask task;

        public NoCleanThread(Player player) {
            this.player = player;
            this.start = System.currentTimeMillis();
            startRunnable();
            threads.put(player, this);
        }

        public Integer getRemainingTime() {
            long now = System.currentTimeMillis();
            long rest = (start + (1000 * 30)) - now;
            return (int) rest / 1000;
        }

        private void startRunnable() {
            this.task = new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(Localization.INSTANCE.getMessage("scenario.noclean.end",  ChatUtils.locale(player)));
                    threads.remove(player);
                }
            }.runTaskLater(Uhc.getPlugin(), 20 * 30);
        }
    }
}
