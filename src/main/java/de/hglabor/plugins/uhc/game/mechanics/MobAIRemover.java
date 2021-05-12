package de.hglabor.plugins.uhc.game.mechanics;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobAIRemover implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Animals) {
            ((Animals) event.getEntity()).setAI(false);
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (!(event.getEntity() instanceof Animals)) {
            return;
        }
        if (event.getEntity() instanceof Horse) {
            return;
        }
        if (!event.getEntity().hasAI()) {
            return;
        }
        event.getEntity().setAI(false);
        event.getEntity().setCustomName("No AI = No Lag :)");
        event.getEntity().setCustomNameVisible(true);
    }
}
