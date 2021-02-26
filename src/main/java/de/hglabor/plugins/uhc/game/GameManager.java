package de.hglabor.plugins.uhc.game;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.phases.LobbyPhase;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    private final Set<Scenario> scenarios;
    private final AtomicInteger timer;
    private GamePhase phase;
    private int border;

    private GameManager() {
        this.timer = new AtomicInteger();
        this.phase = new LobbyPhase();
        this.scenarios = new HashSet<>();
    }

    public void run() {
        phase.init();
        Bukkit.getScheduler().runTaskTimer(Uhc.getPlugin(), () -> {
            final int CURRENT_TIME = timer.getAndIncrement();
            phase.tick(CURRENT_TIME);
            // ScoreboardManager.updateForEveryone(phase.getTimeString(CURRENT_TIME));
            // StaffModeManager.INSTANCE.getPlayerHider().sendHideInformation();
        }, 0, 20L);
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public PhaseType getPhaseType() {
        return getPhase().getType();
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
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

    public void setTimer(int value) {
        timer.set(value);
    }

    public int getTimer() {
        return timer.get();
    }
}
