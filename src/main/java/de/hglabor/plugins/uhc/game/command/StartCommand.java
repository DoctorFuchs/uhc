package de.hglabor.plugins.uhc.game.command;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.phases.LobbyPhase;
import de.hglabor.utils.noriskutils.ChatUtils;
import de.hglabor.utils.noriskutils.PermissionUtils;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

public class StartCommand {
    public StartCommand() {
        new CommandAPICommand("start")
                .withAliases("fs", "forcestart", "begin")
                .withPermission("hglabor.forcestart")
                .withRequirement(commandSender -> {
                    if (commandSender.isOp()) return true;
                    if (commandSender instanceof Player) {
                        return !PermissionUtils.checkForHigherRank((Player) commandSender);
                    }
                    return true;
                })
                .withRequirement((commandSender) -> GameManager.INSTANCE.getPhaseType().equals(PhaseType.LOBBY))
                .executesPlayer((player, objects) -> {
                    LobbyPhase lobbyPhase = (LobbyPhase) GameManager.INSTANCE.getPhase();
                    lobbyPhase.startNextPhase();
                }).register();
    }
}
