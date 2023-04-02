package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Gives the player the rank "live" in the tablist
 */
public class LiveCommand implements CraftProtectCommand {

    private final CraftProtectPlugin plugin;

    public LiveCommand(CraftProtectPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("cp4.streamer")) {
            plugin.sendMessage(sender, M.NO_PERM);
            return true;
        }
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        return true;
    }


}