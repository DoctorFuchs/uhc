package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.player.UserStatus;

public class LobbyPhase extends GamePhase {
    public LobbyPhase() {
        super(60);
    }

    @Override
    protected void tick(int timer) {

    }

    @Override
    public PhaseType getType() {
        return PhaseType.LOBBY;
    }

    @Override
    public int getAlivePlayers() {
        return (int) playerList.getAllPlayers().stream().filter(player -> player.getStatus().equals(UserStatus.LOBBY)).count();
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
