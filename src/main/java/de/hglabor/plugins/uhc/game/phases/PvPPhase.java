package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.mechanics.Border;
import de.hglabor.utils.noriskutils.PotionUtils;
import de.hglabor.utils.noriskutils.TimeConverter;

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
}
