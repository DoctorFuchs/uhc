package de.hglabor.plugins.uhc;

import de.hglabor.plugins.uhc.command.GlobalChatCommand;
import de.hglabor.plugins.uhc.command.InfoCommand;
import de.hglabor.plugins.uhc.command.StartCommand;
import de.hglabor.plugins.uhc.command.WorldTp;
import de.hglabor.plugins.uhc.config.CKeys;
import de.hglabor.plugins.uhc.config.UHCConfig;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.mechanics.chat.GlobalChat;
import de.hglabor.plugins.uhc.game.mechanics.GoldenHead;
import de.hglabor.plugins.uhc.game.mechanics.HeartDisplay;
import de.hglabor.plugins.uhc.game.mechanics.MobRemover;
import de.hglabor.plugins.uhc.game.scenarios.*;
import de.hglabor.plugins.uhc.scoreboard.ScoreboardManager;
import de.hglabor.utils.localization.Localization;
import de.hglabor.utils.noriskutils.DataPackUtils;
import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Bukkit;
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
        DataPackUtils.generateNewWorld(UHCConfig.getString(CKeys.SERVER_FOLDER_PATH), "de.hglabor.uhc");

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
        gameManager.addScenario(DoubleHealth.INSTANCE);
        gameManager.addScenario(FlowerPower.INSTANCE);
        gameManager.addScenario(Shieldless.INSTANCE);
        gameManager.enableScenarios();
        gameManager.run();

        GoldenHead.INSTANCE.register();

        CommandAPI.onEnable(this);
        registerCommand();
        registerListener();
        if (UHCConfig.getBoolean(CKeys.PREGEN_WORLD)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky cancel");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky confirm");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky world world");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky worldborder");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky start");
        }
    }

    public void registerCommand() {
        new StartCommand();
        new GlobalChatCommand();
        new InfoCommand();
        new WorldTp();
    }

    public void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(ScoreboardManager.INSTANCE, this);
        pluginManager.registerEvents(GoldenHead.INSTANCE, this);
        pluginManager.registerEvents(GlobalChat.INSTANCE, this);
        pluginManager.registerEvents(HeartDisplay.INSTANCE, this);
        pluginManager.registerEvents(MobRemover.INSTANCE, this);
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
