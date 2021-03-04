package de.hglabor.plugins.uhc.command;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.phases.LobbyPhase;
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
                    switch (GameManager.INSTANCE.getPhaseType()) {
                        case LOBBY:
                            LobbyPhase lobbyPhase = (LobbyPhase) GameManager.INSTANCE.getPhase();
                            lobbyPhase.startNextPhase();
                        case SCATTERING:
                            GameManager.INSTANCE.getPhase().startNextPhase();
                            break;
                    }
                }).register();
    }
}
