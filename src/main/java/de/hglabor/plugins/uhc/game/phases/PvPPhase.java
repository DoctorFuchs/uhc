package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.border.Border;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.utils.noriskutils.PotionUtils;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PvPPhase extends GamePhase {

    protected PvPPhase() {
        super(0, PhaseType.PVP);
    }

    @Override
    protected void init() {
        playerList.getAlivePlayers().forEach(player -> player.getBukkitPlayer().ifPresent(PotionUtils::removePotionEffects));
    }

    @Override
    protected void tick(int timer) {
        Border border = GameManager.INSTANCE.getBorder();
        border.announceBorderShrink(timer);
        if (timer == border.getNextShrink()) {
            border.run();
        }
    }

    @Override
    public String getTimeString(int timer) {
        String duration = "Duration: ";
        if (timer >= 3600) {
            return duration + TimeConverter.stringify(timer, "%02d:%02d:%02d");
        } else {
            return duration + TimeConverter.stringify(timer);
        }
    }

    @Override
    protected GamePhase getNextPhase() {
        return null;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            UHCPlayer player = playerList.getPlayer((Player) event.getEntity());
            if (player.isTeleporting()) event.setCancelled(true);
        }
    }
}
