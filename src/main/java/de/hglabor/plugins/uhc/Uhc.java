package de.hglabor.plugins.uhc;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.command.StartCommand;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.plugins.uhc.game.scenarios.Netherless;
import de.hglabor.plugins.uhc.game.scenarios.Teams;
import de.hglabor.plugins.uhc.scoreboard.ScoreboardManager;
import de.hglabor.utils.localization.Localization;
import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Paths;

public final class Uhc extends JavaPlugin {
    private static Uhc instance;

    public static Uhc getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        Bukkit.createWorld(new WorldCreator("lobby"));

        GameManager gameManager = GameManager.INSTANCE;
        gameManager.addScenario(Netherless.INSTANCE);
        gameManager.addScenario(Teams.INSTANCE);
        gameManager.run();

        CommandAPI.onEnable(this);
        registerCommand();
        registerListener();
    }

    public void registerCommand() {
        new StartCommand();
    }

    public void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(ScoreboardManager.INSTANCE, this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {
        instance = this;
        UHCConfig.load();
        CommandAPI.onLoad(true);
        Localization.INSTANCE.loadLanguageFiles(Paths.get(this.getDataFolder() + "/lang"), "\u00A7");
    }
}
