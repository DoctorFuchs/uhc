package de.hglabor.plugins.uhc.game.config;

import de.hglabor.plugins.uhc.Uhc;

public class UHCConfig {

    public static void load() {
        Uhc plugin = Uhc.getPlugin();

        //FARM PHASE
        plugin.getConfig().addDefault(CKeys.FARM_FARM_TIME, 60 * 20);
        plugin.getConfig().addDefault(CKeys.FARM_FINAL_HEAL, 60 * 10);

        //BORDER
        plugin.getConfig().addDefault(CKeys.BORDER_MAX_SIZE, 5000);
        plugin.getConfig().addDefault(CKeys.BORDER_SHRINK_SIZE, 500);
        plugin.getConfig().addDefault(CKeys.BORDER_START_SIZE, 2000);
        plugin.getConfig().addDefault(CKeys.BORDER_FIRST_SHRINK, 45 * 60);
        plugin.getConfig().addDefault(CKeys.BORDER_SHRINK_INTERVAL, 60 * 5);

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }


    public static int getInteger(String key) {
        return Uhc.getPlugin().getConfig().getInt(key);
    }

    public static String getString(String key) {
        return Uhc.getPlugin().getConfig().getString(key);
    }

    public static double getDouble(String key) {
        return Uhc.getPlugin().getConfig().getDouble(key);
    }

    public static boolean getBoolean(String key) {
        return Uhc.getPlugin().getConfig().getBoolean(key);
    }
}
