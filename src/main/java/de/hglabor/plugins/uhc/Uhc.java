package de.hglabor.plugins.uhc;

import de.hglabor.plugins.uhc.command.GlobalChatCommand;
import de.hglabor.plugins.uhc.command.StartCommand;
import de.hglabor.plugins.uhc.config.UHCConfig;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.mechanics.GoldenHead;
import de.hglabor.plugins.uhc.game.mechanics.HeartDisplay;
import de.hglabor.plugins.uhc.game.scenarios.*;
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
        gameManager.addScenario(BloodDiamondsNetherite.INSTANCE);
        gameManager.addScenario(CrossBowless.INSTANCE);
        gameManager.addScenario(CutClean.INSTANCE);
        gameManager.addScenario(Fireless.INSTANCE);
        gameManager.addScenario(HasteyBoys.INSTANCE);
        gameManager.addScenario(Netherless.INSTANCE);
        gameManager.addScenario(RodKnockback.INSTANCE);
        gameManager.addScenario(Teams.INSTANCE);
        gameManager.addScenario(AppleDrop.INSTANCE);
        gameManager.addScenario(Timebomb.INSTANCE);
        gameManager.addScenario(Soup.INSTANCE);
        gameManager.addScenario(NoCooldown.INSTANCE);
        gameManager.addScenario(NoClean.INSTANCE);
        gameManager.addScenario(Timber.INSTANCE);
        gameManager.addScenario(Diamondless.INSTANCE);
        gameManager.addScenario(Enchantmentless.INSTANCE);
        gameManager.addScenario(ColdWeapons.INSTANCE);
        gameManager.addScenario(HealingKill.INSTANCE);
        gameManager.addScenario(Horseless.INSTANCE);
        gameManager.addScenario(EnchantedDeath.INSTANCE);
        gameManager.addScenario(DoubleOres.INSTANCE);
        gameManager.run();

        GoldenHead.INSTANCE.register();

        CommandAPI.onEnable(this);
        registerCommand();
        registerListener();
        //   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fcp fillvanilla 0 world");
    }

    public void registerCommand() {
        new StartCommand();
        new GlobalChatCommand();
    }

    public void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(ScoreboardManager.INSTANCE, this);
        pluginManager.registerEvents(GoldenHead.INSTANCE, this);
        pluginManager.registerEvents(HeartDisplay.INSTANCE, this);
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
