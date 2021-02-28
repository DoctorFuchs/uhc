package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;

public class NoCooldown extends Scenario {
    public final static NoCooldown INSTANCE = new NoCooldown();

    private NoCooldown() {
        super("No Cooldown", new ItemBuilder(Material.DIAMOND_SWORD).build());
    }
}
