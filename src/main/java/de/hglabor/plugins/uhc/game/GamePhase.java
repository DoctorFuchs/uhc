package de.hglabor.plugins.uhc.game;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GamePhase implements Listener {
    protected final JavaPlugin plugin;
    protected final PlayerList playerList;
    protected final int maxPhaseTime;
    protected int border;

    protected GamePhase(int maxPhaseTime) {
        this.maxPhaseTime = maxPhaseTime;
        this.plugin = Uhc.getPlugin();
        this.playerList = PlayerList.INSTANCE;
    }

    protected void startNextPhase() {
        HandlerList.unregisterAll(this);
        GamePhase nextPhase = getNextPhase();
        nextPhase.init();
        Bukkit.getPluginManager().registerEvents(nextPhase, plugin);
        GameManager.INSTANCE.setPhase(nextPhase);
    }

    protected void init() {
    }

    protected abstract void tick(int timer);

    public abstract PhaseType getType();

    public int getRawTime() {
        return GameManager.INSTANCE.getTimer();
    }

    public int getBorder() {
        return border;
    }

    protected abstract String getTimeString(int timer);

    public int getAlivePlayers() {
        return (int) playerList.getAllPlayers().stream().filter(UHCPlayer::isAlive).count();
    }

    protected abstract GamePhase getNextPhase();

    public int getMaxPhaseTime() {
        return maxPhaseTime;
    }
}
