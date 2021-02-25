package de.hglabor.plugins.uhc;

import org.bukkit.plugin.java.JavaPlugin;

public final class Uhc extends JavaPlugin {
    private static Uhc instance;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    public static Uhc getPlugin() {
        return instance;
    }
}
