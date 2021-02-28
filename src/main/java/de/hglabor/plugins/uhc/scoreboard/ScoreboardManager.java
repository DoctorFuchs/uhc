package de.hglabor.plugins.uhc.scoreboard;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.utils.noriskutils.TimeConverter;
import de.hglabor.utils.noriskutils.scoreboard.ScoreboardFactory;
import de.hglabor.utils.noriskutils.scoreboard.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;

public final class ScoreboardManager implements Listener {
    public final static ScoreboardManager INSTANCE = new ScoreboardManager();

    private ScoreboardManager() {
    }

    public static void setBasicScoreboardLayout(ScoreboardPlayer player) {
        String placeHolder = ChatColor.STRIKETHROUGH + "                   ";
        ScoreboardFactory.addEntry(player, "placeHolder2", placeHolder, 5);
        ScoreboardFactory.addEntry(player, "time", "Start: " + TimeConverter.stringify(180), 4);
        ScoreboardFactory.addEntry(player, "players", "Players: " + Bukkit.getOnlinePlayers().size(), 3);
        ScoreboardFactory.addEntry(player, "kills", "Kills: 0", 2);
        ScoreboardFactory.addEntry(player, "border", "Border: " + GameManager.INSTANCE.getBorder().getBorderSize(), 1);
        ScoreboardFactory.addEntry(player, "placeHolder1", placeHolder, 0);
    }

    public static void updateForEveryone(int time) {
        for (UHCPlayer uhcPlayer : PlayerList.INSTANCE.getAllPlayers()) {
            uhcPlayer.getBukkitPlayer().ifPresent(player -> {
                GamePhase phase = GameManager.INSTANCE.getPhase();
                ScoreboardFactory.updateEntry(uhcPlayer, "time", phase.getTimeString(time));
                ScoreboardFactory.updateEntry(uhcPlayer, "players", ChatColor.AQUA + "Players: " + ChatColor.GREEN + phase.getAlivePlayers());
                ScoreboardFactory.updateEntry(uhcPlayer, "kills", ChatColor.AQUA + "Kills: " + ChatColor.GREEN + uhcPlayer.getKills().get());
                ScoreboardFactory.updateEntry(uhcPlayer, "border", GameManager.INSTANCE.getBorder().getBorderString(time));
            });
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = PlayerList.INSTANCE.getPlayer(player);
        if (uhcPlayer.getScoreboard() == null) {
            ScoreboardFactory.create(uhcPlayer);
            Objective objective = uhcPlayer.getScoreboard().registerNewObjective("health", "health", ChatColor.RED + "❤", RenderType.HEARTS);
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            ScoreboardManager.setBasicScoreboardLayout(uhcPlayer);
        }
        player.setScoreboard(uhcPlayer.getScoreboard());
    }
}
