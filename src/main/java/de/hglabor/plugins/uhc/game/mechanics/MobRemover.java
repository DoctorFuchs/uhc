package de.hglabor.plugins.uhc.game.mechanics;

import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobRemover implements Listener {
    public final static MobRemover INSTANCE = new MobRemover();
   /* private final Set<EntityType> disabledTypes;

    private MobRemover() {
        this.disabledTypes = Set.of(
                EntityType.RABBIT, EntityType.BEE, EntityType.POLAR_BEAR,
                EntityType.TURTLE, EntityType.SQUID, EntityType.VILLAGER,
                EntityType.PILLAGER, EntityType.COD, EntityType.DOLPHIN,
                EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.CAT,
                EntityType.IRON_GOLEM, EntityType.OCELOT, EntityType.FOX,
                EntityType.WANDERING_TRADER, EntityType.LLAMA, EntityType.SALMON);
    }

    public void enable(World world) {
        for (Entity entity : world.getEntities()) {
            if (disabledTypes.contains(entity.getType())) {
                entity.remove();
            } else if (entity instanceof Animals) {
                ((Animals) entity).setAI(false);
            }
        }
    }

    @EventHandler
    private void onPreCreatureSpawn(PreCreatureSpawnEvent event) {
        if (disabledTypes.contains(event.getType())) {
            event.setShouldAbortSpawn(true);
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Animals) {
            event.getEntity().setAI(false);
        }
    }  */
}
