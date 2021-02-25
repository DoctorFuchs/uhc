package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.PortalCreateEvent;

public class Netherless extends Scenario {
    public final static Netherless INSTANCE = new Netherless();

    private Netherless() {
        super("Netherless", new ItemBuilder(Material.NETHERRACK).build());
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (!isEnabled()) {
            if (event.getReason().equals(PortalCreateEvent.CreateReason.FIRE)) {
                event.setCancelled(true);
            }
        }
    }
}
