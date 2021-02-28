package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;

public class DoubleHealth extends Scenario {
    public final static DoubleHealth INSTANCE = new DoubleHealth();

    private DoubleHealth() {
        super("Double Health", new ItemBuilder(Material.POPPY).build());
    }
}
