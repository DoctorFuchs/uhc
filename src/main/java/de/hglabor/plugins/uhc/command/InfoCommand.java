package de.hglabor.plugins.uhc.command;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.plugins.uhc.game.mechanics.border.Border;
import de.hglabor.plugins.uhc.game.phases.FarmPhase;
import de.hglabor.utils.noriskutils.TimeConverter;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

public class InfoCommand {
    public InfoCommand() {
        new CommandAPICommand("info")
                .withAliases("scenarios", "help", "game", "settings")
                .executesPlayer((player, objects) -> {
                    String strike = ChatColor.RESET.toString() + ChatColor.STRIKETHROUGH + "               ";
                    player.sendMessage(strike + ChatColor.RESET + ChatColor.AQUA + "UHC" + strike);
                    switch (GameManager.INSTANCE.getPhaseType()) {
                        case LOBBY:
                            player.sendMessage(GameManager.INSTANCE.getPhase().getTimeString(GameManager.INSTANCE.getTimer()));
                            sendScenarios(player);
                            break;
                        case FARM:
                            FarmPhase phase = (FarmPhase) GameManager.INSTANCE.getPhase();
                            int totalSecs = phase.getFinalHeal() - GameManager.INSTANCE.getTimer();
                            if (totalSecs < 0) {
                                player.sendMessage(ChatColor.AQUA + "Final Heal: " + ChatColor.GREEN + "✔");
                            } else {
                                player.sendMessage(ChatColor.AQUA + "Final Heal: " + ChatColor.GREEN + TimeConverter.stringify(totalSecs));
                            }
                            player.sendMessage(ChatColor.AQUA + "PvP in: " + ChatColor.GREEN + TimeConverter.stringify(phase.getMaxPhaseTime() - GameManager.INSTANCE.getTimer()));
                            sendScenarios(player);
                            break;
                        case PVP:
                            Border border = GameManager.INSTANCE.getBorder();
                            int timeLeft = border.getNextShrinkTime() - GameManager.INSTANCE.getTimer();
                            if (timeLeft >= 0) {
                                player.sendMessage(ChatColor.AQUA + "Current Border: " + ChatColor.GREEN + border.getBorderSize());
                                player.sendMessage(ChatColor.AQUA + "Next Border: " + ChatColor.GREEN + border.getNextBorderSize() + " in " + TimeConverter.stringify(timeLeft));
                            } else {
                                player.sendMessage(ChatColor.AQUA + "Final Border: " + ChatColor.GREEN + border.getBorderSize());
                            }
                            sendScenarios(player);
                            break;

                    }
                }).register();
    }

    private void sendScenarios(Player player) {
        Set<Scenario> scenarios = GameManager.INSTANCE.getScenarios();
        scenarios.stream().filter(Scenario::isEnabled).map(scenario -> ChatColor.GREEN + " - " + ChatColor.BLUE + scenario.getName()).forEach(player::sendMessage);
    }
}
