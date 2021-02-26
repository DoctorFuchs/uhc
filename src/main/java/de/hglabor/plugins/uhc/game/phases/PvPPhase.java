package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.config.CKeys;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.utils.noriskutils.PotionUtils;
import de.hglabor.utils.noriskutils.TimeConverter;

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
        playerList.getAlivePlayers().forEach(player -> player.getBukkitPlayer().ifPresent(PotionUtils::removePotionEffects));
    }

    @Override
    protected void tick(int timer) {

    }

    @Override
    public String getTimeString(int timer) {
        if (timer >= 3600) {
            return TimeConverter.stringify(timer, "%02d:%02d:%02d");
        } else {
            return TimeConverter.stringify(timer);
        }
    }

    @Override
    protected GamePhase getNextPhase() {
        return null;
    }
}
