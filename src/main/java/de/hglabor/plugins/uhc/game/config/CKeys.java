package de.hglabor.plugins.uhc.game.config;

public interface CKeys {
    String PHASE = "phase";

    String FARM = PHASE + "." + "farm";
    String FARM_FINAL_HEAL = FARM + "." + "finalHeal";
    String FARM_FARM_TIME = FARM + "." + "farmTime";

    String PVP = PHASE + "." + "pvp";
    String PVP_FIRST_SHRINK = PHASE + "." + "firstShrink";
    String PVP_SHRINK_INTERVAL = PHASE + "." + "shrinkInterval";

    String BORDER = "border";
    String BORDER_MAX_SIZE = "border" + "." + "maxSize";

}
