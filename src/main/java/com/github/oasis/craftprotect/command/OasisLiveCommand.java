package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class OasisLiveCommand implements CraftProtectCommand {

    @Inject
    private CraftProtect plugin;

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("cp4.admin")) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            plugin.sendMessage(onlinePlayer, "command.oasislive.broadcast");
        }
        return true;
    }

}
