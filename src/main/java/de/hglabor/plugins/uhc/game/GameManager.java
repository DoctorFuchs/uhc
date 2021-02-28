package de.hglabor.plugins.uhc.game;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.config.CKeys;
import de.hglabor.plugins.uhc.config.UHCConfig;
import de.hglabor.plugins.uhc.game.mechanics.border.Border;
import de.hglabor.plugins.uhc.game.phases.LobbyPhase;
import de.hglabor.plugins.uhc.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    private final Set<Scenario> scenarios;
    private final AtomicInteger timer;
    private final Border border;
    private GamePhase phase;

    private GameManager() {
        this.timer = new AtomicInteger();
        this.phase = new LobbyPhase();
        this.border = new Border();
        this.scenarios = new HashSet<>();
    }

    public void run() {
        phase.init();
        Bukkit.getScheduler().runTaskTimer(Uhc.getPlugin(), () -> {
            final int CURRENT_TIME = timer.getAndIncrement();
            phase.tick(CURRENT_TIME);
            ScoreboardManager.updateForEveryone(CURRENT_TIME);
            // StaffModeManager.INSTANCE.getPlayerHider().sendHideInformation();
        }, 0, 20L);
    }

    public void enableScenarios() {
        for (Scenario scenario : scenarios) {
            scenario.setEnabled(UHCConfig.getBoolean(CKeys.SCENARIOS + "." + scenario.getName() + "." + "enabled"));
            if (scenario.isEnabled()) {
                Bukkit.getPluginManager().registerEvents(scenario, Uhc.getPlugin());
            }
        }
    }

    public Set<Scenario> getScenarios() {
        return scenarios;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public PhaseType getPhaseType() {
        return getPhase().getType();
    }

    public Border getBorder() {
        return border;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public void resetTimer() {
        timer.set(0);
    }

    public int getTimer() {
        return timer.get();
    }

    public void setTimer(int value) {
        timer.set(value);
    }
}
