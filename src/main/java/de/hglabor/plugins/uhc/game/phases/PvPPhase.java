package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.utils.noriskutils.PotionUtils;

public class PvPPhase extends GamePhase {
    protected PvPPhase() {
        super(60*60, PhaseType.PVP);
    }

    @Override
    protected void init() {
        playerList.getAlivePlayers().forEach(player -> player.getPlayer().ifPresent(PotionUtils::removePotionEffects));
    }

    @Override
    protected void tick(int timer) {

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
