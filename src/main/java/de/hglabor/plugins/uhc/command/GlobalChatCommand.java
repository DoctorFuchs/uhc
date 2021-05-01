package de.hglabor.plugins.uhc.command;

import de.hglabor.plugins.uhc.game.mechanics.chat.GlobalChat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;

public class GlobalChatCommand {
    public GlobalChatCommand() {
        new CommandAPICommand("globalchat")
                .withPermission("group.mod")
                .withArguments(new BooleanArgument("enable"))
                .executesPlayer((player, objects) -> {
                    GlobalChat.INSTANCE.enable((Boolean) objects[0]);
                }).register();
    }
}
