package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.plugins.uhc.game.mechanics.UHCTeam;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Teams extends Scenario {
    public final static Teams INSTANCE = new Teams();

    private int size;
    private final Map<Integer, UHCTeam> teams;

    private Teams() {
        super("Teams", new ItemBuilder(Material.GOLD_BLOCK).build());
        this.teams = new HashMap<>();
    }
}
