package de.hglabor.plugins.uhc.util;

import de.hglabor.plugins.uhc.game.mechanics.border.Corner;
import de.hglabor.utils.noriskutils.ChanceUtils;
import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;

public final class SpawnUtils {

    private SpawnUtils() {
    }

    public static Location getCornerSpawn(Corner corner, int borderSize) {
        World world = Bukkit.getWorld("world");
        int x = corner.xConverter.convert(ChanceUtils.getRandomNumber(borderSize, 0));
        int z = corner.zConverter.convert(ChanceUtils.getRandomNumber(borderSize, 0));
        return new Location(world, x, world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z);
    }
}
