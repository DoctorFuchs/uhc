package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.player.UHCPlayer;

import java.util.HashSet;
import java.util.Set;

public class UHCTeam {
    private final int size;
    private Set<UHCPlayer> team;

    public UHCTeam(int teamSize) {
        this.size = teamSize;
        this.team = new HashSet<>();
    }
}
