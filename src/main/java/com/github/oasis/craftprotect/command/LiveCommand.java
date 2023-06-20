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
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Gives the player the rank "live" in the tab list
 */
@Singleton
public class LiveCommand implements CraftProtectCommand, CommandExecutor {

    @Inject
    private CraftProtectPlugin plugin;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.live";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        String str = String.format(Arrays.toString(args));
        Bukkit.broadcastMessage(sender + "Ist jetzt Live: " + str);

        return true;
    }


}