package de.hglabor.plugins.uhc.game.mechanics;

import java.util.Random;

public enum Corner {
    POS_POS(Math::abs, Math::abs),
    NEG_NEG(value -> Math.abs(value) * -1, value -> Math.abs(value) * -1),
    POS_NEG(Math::abs, value -> Math.abs(value) * -1),
    NEG_POS(value -> Math.abs(value) * -1, Math::abs);

    public final Converter xConverter;
    public final Converter zConverter;

    Corner(Converter x, Converter z) {
        this.xConverter = x;
        this.zConverter = z;
    }

    @FunctionalInterface
    public interface Converter {
        int convert(int value);
    }

    public static Corner getCorner(int corner) {
        switch (corner) {
            case 1:
                return POS_POS;
            case 2:
                return NEG_POS;
            case 3:
                return NEG_NEG;
            case 4:
                return POS_NEG;
            default:
                return values()[new Random().nextInt(values().length)];
        }
    }
}
