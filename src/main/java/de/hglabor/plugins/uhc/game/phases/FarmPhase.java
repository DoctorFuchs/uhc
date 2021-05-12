package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.config.CKeys;
import de.hglabor.plugins.uhc.config.UHCConfig;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.plugins.uhc.game.mechanics.CombatLogger;
import de.hglabor.plugins.uhc.game.mechanics.HeartDisplay;
import de.hglabor.plugins.uhc.game.mechanics.border.Border;
import de.hglabor.plugins.uhc.game.mechanics.chat.BroadcastType;
import de.hglabor.plugins.uhc.game.mechanics.chat.GlobalChat;
import de.hglabor.utils.noriskutils.PotionUtils;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class FarmPhase extends IngamePhase {
    private final int finalHeal;
    private final int broadcastSwitchTime;
    private int broadcastTime;
    private boolean wasFinalHeal;
    private BroadcastType currentBroadcast;

    protected FarmPhase() {
        super(UHCConfig.getInteger(CKeys.FARM_FARM_TIME), PhaseType.FARM);
        this.finalHeal = UHCConfig.getInteger(CKeys.FARM_FINAL_HEAL);
        this.currentBroadcast = BroadcastType.BORDER;
        this.broadcastSwitchTime = UHCConfig.getInteger(CKeys.BROADCAST_NEW_TYPE);
    }

    @Override
    protected void init() {
        GameManager.INSTANCE.enableScenarios();
        Bukkit.getPluginManager().registerEvents(CombatLogger.INSTANCE, Uhc.getPlugin());
        Bukkit.broadcastMessage(GlobalChat.getPrefix() + GlobalChat.hexColor("#F45959") + "You are now able to relog");
        for (Player player : Bukkit.getOnlinePlayers()) {
            HeartDisplay.INSTANCE.enableHealthBar(player);
            player.sendTitle( GlobalChat.hexColor("#EC2828") + "UHC" + ChatColor.WHITE + " | " + GlobalChat.hexColor("#F45959") + "Farmphase",
                    ChatColor.GOLD + "gl hf", 20, 20, 20);
            player.setHealth(20);
            player.setSaturation(20);
            player.setFireTicks(0);
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 20));
            PotionUtils.removePotionEffects(player);
        }
        //SCENARIOS
        GameManager.INSTANCE.getScenarios().stream().filter(Scenario::isEnabled).forEach(Scenario::onPvPPhase);
    }

    @Override
    protected void tick(int timer) {
        announceNextPhase(timer);
        handleFinalHeal(timer);

        handleBroadcast(timer);
        if (timer > maxPhaseTime) {
            this.startNextPhase();
        }
    }

    /**
     * lol
     */
    private void handleBroadcast(int timer) {
        switch (currentBroadcast) {
            case BORDER:
                Bukkit.getOnlinePlayers().forEach(player -> {
                    Border border = GameManager.INSTANCE.getBorder();
                    player.sendActionBar( GlobalChat.hexColor("#EC2828") + "Next bordershrink " + border.getNextBorderSize() + " in: " + ChatColor.GRAY + TimeConverter.stringify(border.getNextShrinkTime() - timer));
                });
                break;
            case FINALHEAL:
                Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar( GlobalChat.hexColor("#EC2828") + "Final-Heal in: " + ChatColor.GRAY + TimeConverter.stringify(finalHeal - timer)));
                break;
            case INVINCIBILITY:
                Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar( GlobalChat.hexColor("#EC2828") + "Schutzzeit endet in: " + ChatColor.GRAY + TimeConverter.stringify(maxPhaseTime - timer)));
                break;
        }

        broadcastTime++;

        if (broadcastTime > broadcastSwitchTime) {
            switch (currentBroadcast) {
                case BORDER:
                    currentBroadcast = wasFinalHeal ? BroadcastType.INVINCIBILITY : BroadcastType.FINALHEAL;
                    break;
                case FINALHEAL:
                    currentBroadcast = BroadcastType.INVINCIBILITY;
                    break;
                case INVINCIBILITY:
                    currentBroadcast = BroadcastType.BORDER;
                    break;
            }
            broadcastTime = 0;
        }
    }

    private void handleFinalHeal(int timer) {
        if (wasFinalHeal) return;
        int timeLeft = finalHeal - timer;
        if (timeLeft == 0) {
            wasFinalHeal = true;
            Bukkit.getOnlinePlayers().forEach(player -> player.setHealth(20));
            Bukkit.broadcastMessage(GlobalChat.getPrefix() +  GlobalChat.hexColor("#EC2828") + ChatColor.BOLD + "Final Heal");
        } else if (timeLeft % (2 * 60) == 0) {
            String timeString = TimeConverter.stringify(timeLeft);
            Bukkit.broadcastMessage(GlobalChat.getPrefix() +  GlobalChat.hexColor("#EC2828") + "Final-Heal in " + GlobalChat.hexColor("#F45959") + timeString);
        }
    }

    public int getFinalHeal() {
        return finalHeal;
    }

    private void announceNextPhase(int timer) {
        int timeLeft = maxPhaseTime - timer;
        if (timeLeft == 0) {
            Bukkit.broadcastMessage(GlobalChat.getPrefix() +  GlobalChat.hexColor("#EC2828") + ChatColor.BOLD + "PvP has been enabled");
        } else if (timeLeft % (5 * 60) == 0) {
            String timeString = TimeConverter.stringify(timeLeft);
            Bukkit.broadcastMessage(GlobalChat.getPrefix() +  GlobalChat.hexColor("#EC2828") + "PvP enabled in " + GlobalChat.hexColor("#F45959") + timeString);
        }
    }

    @Override
    public String getTimeString(int timer) {
        return  GlobalChat.hexColor("#EC2828") + "Duration: " + GlobalChat.hexColor("#F45959") + TimeConverter.stringify(timer);
    }

    @Override
    protected GamePhase getNextPhase() {
        return new PvPPhase();
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            event.setCancelled(true);
        }
    }
}
