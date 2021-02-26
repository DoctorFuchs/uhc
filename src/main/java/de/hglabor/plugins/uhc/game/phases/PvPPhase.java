package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.config.CKeys;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.utils.noriskutils.PotionUtils;

public class PvPPhase extends GamePhase {
    private final int shrinkInterval;
    private int nextShrink;

    protected PvPPhase() {
        super(0, PhaseType.PVP);
        this.nextShrink = UHCConfig.getInteger(CKeys.PVP_FIRST_SHRINK);
        this.shrinkInterval = UHCConfig.getInteger(CKeys.PVP_SHRINK_INTERVAL);
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
