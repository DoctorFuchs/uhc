package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoThrowable implements Scenario {
    public final static NoThrowable INSTANCE = new NoThrowable();

    private NoThrowable() {
        super("No-Throwable", new ItemBuild(Material.EGG).build());
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent event) {
        if (!isEnabled()){
            return;
        }
        if (event.getMaterial().equals(Material.EGG) || event.getMaterial().equals(Material.SNOWBALL)) {
            event.setCancelled(true);
        }
    }
}
