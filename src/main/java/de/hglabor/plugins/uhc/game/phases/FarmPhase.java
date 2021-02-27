package de.hglabor.plugins.uhc.game.phases;

import com.google.common.collect.ImmutableMap;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.config.CKeys;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.utils.noriskutils.ChatUtils;
import de.hglabor.utils.noriskutils.PotionUtils;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FarmPhase extends IngamePhase {
    private final int finalHeal;

    protected FarmPhase() {
        super(UHCConfig.getInteger(CKeys.FARM_FARM_TIME), PhaseType.FARM);
        this.finalHeal = UHCConfig.getInteger(CKeys.FARM_FINAL_HEAL);
    }

    @Override
    protected void init() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("UHC | Farmphase", "gl hf", 20, 20, 20);
            PotionUtils.removePotionEffects(player);
        }
    }

    @Override
    protected void tick(int timer) {
        announceNextPhase(timer);
        announceFinalHeal(timer);
        if (timer > maxPhaseTime) {
            this.startNextPhase();
        }
    }

    private void announceFinalHeal(int timer) {
        int timeLeft = finalHeal - timer;
        if (timeLeft % (2 * 60) == 0) {
            String timeString = TimeConverter.stringify(timeLeft);
            ChatUtils.broadcastMessage("farm.finaHealIn", ImmutableMap.of("time", timeString));
        }
    }

    private void announceNextPhase(int timer) {
        int timeLeft = maxPhaseTime - timer;
        if (timeLeft % (5 * 60) == 0) {
            String timeString = TimeConverter.stringify(timeLeft);
            ChatUtils.broadcastMessage("farm.pvpIn", ImmutableMap.of("time", timeString));
        }
    }

    @Override
    public String getTimeString(int timer) {
        return "Duration: " + TimeConverter.stringify(timer);
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
