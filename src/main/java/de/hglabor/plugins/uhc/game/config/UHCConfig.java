package de.hglabor.plugins.uhc.game.config;

import de.hglabor.plugins.uhc.Uhc;
import org.bukkit.GameRule;
import org.bukkit.World;

public class UHCConfig {

    public static void load() {
        Uhc plugin = Uhc.getPlugin();

        //LOBBY
        plugin.getConfig().addDefault(CKeys.LOBBY_START_TIME, 60 * 15);

        //SCATTER
        plugin.getConfig().addDefault(CKeys.SCATTER_TELEPORT_DELAY, 10);

        //FARM PHASE
        plugin.getConfig().addDefault(CKeys.FARM_FARM_TIME, 60 * 20);
        plugin.getConfig().addDefault(CKeys.FARM_FINAL_HEAL, 60 * 10);

        //BORDER
        plugin.getConfig().addDefault(CKeys.BORDER_MAX_SIZE, 5000);
        plugin.getConfig().addDefault(CKeys.BORDER_SHRINK_SIZE, 500);
        plugin.getConfig().addDefault(CKeys.BORDER_START_SIZE, 2000);
        plugin.getConfig().addDefault(CKeys.BORDER_FIRST_SHRINK, 45 * 60);
        plugin.getConfig().addDefault(CKeys.BORDER_SHRINK_INTERVAL, 60 * 5);

        plugin.getConfig().addDefault(CKeys.RELOG_TIME, 120);

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }


    public static void setPvPWorldSettings(World world) {
        world.setTime(6000);
        world.setStorm(false);
        world.setThundering(false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
    }

    public static void setLobbySettings(World world) {
        world.setTime(6000);
        world.setStorm(false);
        world.setThundering(false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.MOB_GRIEFING, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DISABLE_RAIDS, true);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        world.setGameRule(GameRule.DO_TILE_DROPS, false);
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
