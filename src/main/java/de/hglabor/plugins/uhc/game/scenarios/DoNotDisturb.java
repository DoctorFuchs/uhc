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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class DoNotDisturb extends Scenario {
    public final static DoNotDisturb INSTANCE = new DoNotDisturb();

    private DoNotDisturb() {
        super("NoCleanPlus", new ItemBuilder(Material.NETHERITE_LEGGINGS).build());
    }

    static HashMap<Player, NoCleanPlusThread> threads = new HashMap<>();

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (isEnabled()) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                if (threads.containsKey(player)) {
                    if (threads.get(player).getOpponent() != damager) {
                        event.setCancelled(true);
                        damager.sendMessage(Localization.INSTANCE.getMessage("scenario.nocleanplus.playersTimeLeft",
                                ImmutableMap.of("numberInSeconds", String.valueOf(threads.get(player).getRemainingTime())),
                                ChatUtils.locale(player)));
                    } else {
                        threads.get(player).resetRunnable();
                        threads.get(damager).resetRunnable();
                    }
                } else if (threads.containsKey(damager)) {
                    NoCleanPlusThread thread = threads.get(damager);
                    if (thread.getOpponent() != player) {
                        damager.sendMessage(Localization.INSTANCE.getMessage("scenario.nocleanplus.alreadyFighting",
                                ImmutableMap.of("opponent", threads.get(damager).getOpponent().getName()),
                                ChatUtils.locale(player)));
                        event.setCancelled(true);
                    }
                } else {
                    new NoCleanPlusThread(player, damager);
                    new NoCleanPlusThread(damager, player);
                }
            }
        }
    }

    public static class NoCleanPlusThread {
        private final Player player;
        private final Player opponent;
        private final long start;
        private BukkitTask task;

        public NoCleanPlusThread(Player player, Player opponent) {
            this.player = player;
            this.opponent = opponent;
            this.start = System.currentTimeMillis();
            startRunnable();
            threads.put(player, this);
        }

        private void startRunnable() {
            this.task = new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(Localization.INSTANCE.getMessage("scenario.nocleanplus.end", ChatUtils.locale(player)));
                    threads.remove(player);
                }
            }.runTaskLater(Uhc.getPlugin(), 20 * 20);
        }

        public void resetRunnable() {
            this.task.cancel();
            startRunnable();
        }

        public Integer getRemainingTime() {
            long now = System.currentTimeMillis();
            long rest = (start + (1000 * 20)) - now;
            return (int) rest / 1000;
        }

        public Player getOpponent() {
            return opponent;
        }
    }
}