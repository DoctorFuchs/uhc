package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;

public class InGamePhase extends GamePhase {
    protected InGamePhase(int maxPhaseTime, PhaseType type) {
        super(maxPhaseTime, type);
    }

    @Override
    protected void tick(int timer) {

    }

    @Override
    public String getTimeString(int timer) {
        return null;
    }

    @Override
    protected GamePhase getNextPhase() {
        return null;
    }
}
