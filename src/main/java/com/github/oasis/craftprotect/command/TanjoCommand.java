package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.google.inject.Singleton;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class TanjoCommand implements CraftProtectCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        player.kickPlayer("Tanjo");
        return true;
    }
}
