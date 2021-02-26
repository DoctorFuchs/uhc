package de.hglabor.plugins.uhc.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public final class BorderUtils {
    private BorderUtils() {
    }

    public static void createBorder(World world, int size, int startPoint, int height) {
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1)) {
            BlockVector3 negNeg = BukkitAdapter.asBlockVector(new Location(world, -size, startPoint, -size));
            BlockVector3 posPos = BukkitAdapter.asBlockVector(new Location(world, size, startPoint + height, size));
            CuboidRegion region = new CuboidRegion(weWorld, negNeg, posPos);
            editSession.setFastMode(true);
            editSession.makeCuboidWalls(region, BukkitAdapter.asBlockType(Material.BEDROCK));
        }
    }
}
