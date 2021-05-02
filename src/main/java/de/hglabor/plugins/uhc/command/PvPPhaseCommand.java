package de.hglabor.plugins.uhc.command;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.phases.FarmPhase;
import de.hglabor.utils.noriskutils.PermissionUtils;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

public class PvPPhaseCommand {
    public PvPPhaseCommand() {
        new CommandAPICommand("pvpphase")
                .withPermission("hglabor.forcestart")
                .withRequirement(commandSender -> {
                    if (commandSender.isOp()) return true;
                    if (commandSender instanceof Player) {
                        return !PermissionUtils.checkForHigherRank((Player) commandSender);
                    }
                    return true;
                })
                .withRequirement((commandSender) -> GameManager.INSTANCE.getPhaseType().equals(PhaseType.FARM))
                .executesPlayer((player, objects) -> {
                    FarmPhase farmPhase = (FarmPhase) GameManager.INSTANCE.getPhase();
                    farmPhase.startNextPhase();
                }).register();
    }
}
