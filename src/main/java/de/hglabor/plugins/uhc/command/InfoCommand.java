package de.hglabor.plugins.uhc.command;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.plugins.uhc.game.mechanics.border.Border;
import de.hglabor.plugins.uhc.game.mechanics.chat.GlobalChat;
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
                    player.sendMessage(strike + ChatColor.RESET + GlobalChat.hexColor("#EC2828") + "UHC" + strike);
                    switch (GameManager.INSTANCE.getPhaseType()) {
                        case LOBBY:
                            player.sendMessage(GameManager.INSTANCE.getPhase().getTimeString(GameManager.INSTANCE.getTimer()));
                            sendScenarios(player);
                            break;
                        case FARM:
                            FarmPhase phase = (FarmPhase) GameManager.INSTANCE.getPhase();
                            int totalSecs = phase.getFinalHeal() - GameManager.INSTANCE.getTimer();
                            if (totalSecs < 0) {
                                player.sendMessage( GlobalChat.hexColor("#EC2828") + "Final Heal: " + GlobalChat.hexColor("#F45959") + "✔");
                            } else {
                                player.sendMessage( GlobalChat.hexColor("#EC2828") + "Final Heal: " + GlobalChat.hexColor("#F45959") + TimeConverter.stringify(totalSecs));
                            }
                            player.sendMessage( GlobalChat.hexColor("#EC2828") + "PvP in: " + GlobalChat.hexColor("#F45959") + TimeConverter.stringify(phase.getMaxPhaseTime() - GameManager.INSTANCE.getTimer()));
                            sendScenarios(player);
                            break;
                        case PVP:
                            Border border = GameManager.INSTANCE.getBorder();
                            int timeLeft = border.getNextShrinkTime() - GameManager.INSTANCE.getTimer();
                            if (timeLeft >= 0) {
                                player.sendMessage( GlobalChat.hexColor("#EC2828") + "Current Border: " + GlobalChat.hexColor("#F45959") + border.getBorderSize());
                                player.sendMessage( GlobalChat.hexColor("#EC2828") + "Next Border: " + GlobalChat.hexColor("#F45959") + border.getNextBorderSize() + " in " + TimeConverter.stringify(timeLeft));
                            } else {
                                player.sendMessage( GlobalChat.hexColor("#EC2828") + "Final Border: " + GlobalChat.hexColor("#F45959") + border.getBorderSize());
                            }
                            sendScenarios(player);
                            break;

                    }
                }).register();
    }

    private void sendScenarios(Player player) {
        Set<Scenario> scenarios = GameManager.INSTANCE.getScenarios();
        scenarios.stream().filter(Scenario::isEnabled).map(scenario -> GlobalChat.hexColor("#F45959") + " - " + ChatColor.BLUE + scenario.getName()).forEach(player::sendMessage);
    }
}
