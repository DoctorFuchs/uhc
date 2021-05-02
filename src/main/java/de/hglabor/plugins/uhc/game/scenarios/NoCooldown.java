package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class NoCooldown extends Scenario {
    public final static NoCooldown INSTANCE = new NoCooldown();

    private NoCooldown() {
        super("No Cooldown", new ItemBuilder(Material.DIAMOND_SWORD).build());
    }

    @Override
    public void onPvPPhase() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            if (attribute != null) {
                attribute.setBaseValue(100);
            }
        }
    }
}
