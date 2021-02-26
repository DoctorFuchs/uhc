package de.hglabor.plugins.uhc.game.config;

public interface CKeys {
    String PHASE = "phase";

    String FARM = PHASE + "." + "farm";
    String FARM_FINAL_HEAL = FARM + "." + "finalHeal";
    String FARM_FARM_TIME = FARM + "." + "farmTime";

    String PVP = PHASE + "." + "pvp";

    String BORDER = "border";
    String BORDER_FIRST_SHRINK = BORDER + "." + "firstShrink";
    String BORDER_START_SIZE = BORDER + "." + "startSize";
    String BORDER_SHRINK_INTERVAL = BORDER + "." + "shrinkInterval";
    String BORDER_MAX_SIZE = BORDER + "." + "maxSize";
    /**
     * Shrink size til border hits 500 mark
     */
    String BORDER_SHRINK_SIZE = "border" + "." + "shrinkSize";

}
