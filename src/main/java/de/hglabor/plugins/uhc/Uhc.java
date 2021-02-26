package de.hglabor.plugins.uhc;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.scenarios.Netherless;
import de.hglabor.plugins.uhc.game.scenarios.Teams;
import org.bukkit.plugin.java.JavaPlugin;

public final class Uhc extends JavaPlugin {
    private static Uhc instance;

    //TODO Scattering, PvPless Phase, PvPPhase

    @Override
    public void onEnable() {
        GameManager gameManager = GameManager.INSTANCE;
        gameManager.addScenario(Netherless.INSTANCE);
        gameManager.addScenario(Teams.INSTANCE);
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
