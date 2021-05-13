package de.hglabor.plugins.uhc.command;

import de.hglabor.plugins.uhc.config.UHCConfig;
import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.mechanics.chat.GlobalChat;
import de.hglabor.utils.noriskutils.PermissionUtils;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

public class BorderCommand {
    public BorderCommand() {
        new CommandAPICommand("nextborder")
                .withPermission("hglabor.forcestart")
                .withRequirement(commandSender -> {
                    if (commandSender.isOp()) return true;
                    if (commandSender instanceof Player) {
                        return !PermissionUtils.checkForHigherRank((Player) commandSender);
                    }
                    return true;
                })
                .executesPlayer((player, objects) -> {
                    GameManager.INSTANCE.getBorder().run(true);
                    player.sendMessage(GlobalChat.getPrefix() + "Border wurde geshrinkt.");
                }).register();
    }
}
