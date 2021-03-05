package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityMountEvent;

public class Horseless extends Scenario {
    public final static Horseless INSTANCE = new Horseless();

    private Horseless() {
        super("Horseless", new ItemBuilder(Material.SADDLE).build());
    }

    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        if (isEnabled()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (event.getMount() instanceof Horse || event.getMount() instanceof Mule || event.getMount() instanceof Donkey) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
