package de.hglabor.plugins.uhc.game.mechanics.border;

import org.bukkit.Location;

public enum Outside {
    X, Z, BOTH, INSIDE;

    public static Outside get(Location location, int borderSize) {
        double x = Math.abs(location.getX());
        double z = Math.abs(location.getZ());

        if (x >= borderSize && z >= borderSize) {
            return BOTH;
        } else if (x >= borderSize) {
            return X;
        } else if (z >= borderSize) {
            return Z;
        }
        return INSIDE;
    }
}
