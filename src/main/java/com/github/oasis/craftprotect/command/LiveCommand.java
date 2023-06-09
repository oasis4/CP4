package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Gives the player the rank "live" in the tablist
 */
@Singleton
public class LiveCommand implements CraftProtectCommand, CommandExecutor {

    @Inject
    private CraftProtectPlugin plugin;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("*")) {
            plugin.sendMessage(sender, M.NO_PERM);
            return true;
        }
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        String str = String.format(Arrays.toString(args));
        Bukkit.broadcastMessage(sender + "Ist jetzt Live: " + str);

        return true;
    }


}