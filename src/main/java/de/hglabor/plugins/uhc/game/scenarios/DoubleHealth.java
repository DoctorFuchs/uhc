package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class DoubleHealth extends Scenario {
    public final static DoubleHealth INSTANCE = new DoubleHealth();

    private DoubleHealth() {
        super("Double Health", new ItemBuilder(Material.POPPY).build());
    }

    @Override
    public void onPvPPhase() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(40);
                player.setHealth(40);
            }
        }
    }
}
