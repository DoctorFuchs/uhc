package de.hglabor.plugins.uhc.game.mechanics;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import de.hglabor.utils.noriskutils.ChanceUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

public class MobRemover implements Listener {
    public final static MobRemover INSTANCE = new MobRemover();
    private final Set<EntityType> disabledTypes;

    private MobRemover() {
        this.disabledTypes = Set.of(
                EntityType.RABBIT, EntityType.BEE, EntityType.POLAR_BEAR,
                EntityType.TURTLE, EntityType.SQUID, EntityType.VILLAGER,
                EntityType.PILLAGER, EntityType.COD, EntityType.DOLPHIN,
                EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.CAT,
                EntityType.IRON_GOLEM, EntityType.OCELOT, EntityType.FOX,
                EntityType.WANDERING_TRADER, EntityType.LLAMA, EntityType.SALMON);
    }

    public void killMobs() {
        Bukkit.getWorld("world").getEntities().stream().filter(entity -> disabledTypes.contains(entity.getType())).forEach(Entity::remove);
    }

    @EventHandler
    private void onPreCreatureSpawn(PreCreatureSpawnEvent event) {
        if (disabledTypes.contains(event.getType())) {
            event.setShouldAbortSpawn(true);
            event.setCancelled(true);
        } else if (event.getType().equals(EntityType.SHEEP)) {
            if (ChanceUtils.roll(65)) {
                event.setShouldAbortSpawn(true);
                event.setCancelled(true);
            }
        }
    }
}
