package de.hglabor.plugins.uhc.game.config;

import de.hglabor.plugins.uhc.Uhc;

public class UHCConfig {

    public static void load() {
        Uhc plugin = Uhc.getPlugin();
     //   plugin.getConfig().addDefault(ConfigKeys.LOBBY_PLAYERS_NEEDED, 2);

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
