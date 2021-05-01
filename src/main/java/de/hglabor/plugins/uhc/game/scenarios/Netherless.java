package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class Netherless extends Scenario {
    public final static Netherless INSTANCE = new Netherless();

    private Netherless() {
        super("Netherless", new ItemBuilder(Material.NETHERRACK).build());
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (!isEnabled()) {
            return;
        }
        if (event.getReason().equals(PortalCreateEvent.CreateReason.FIRE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (!isEnabled()) {
            return;
        }

        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
            event.setCancelled(true);
        }
    }
}
