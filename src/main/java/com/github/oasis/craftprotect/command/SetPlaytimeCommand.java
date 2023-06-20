package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.controller.PlaytimeController;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class SetPlaytimeCommand implements CraftProtectCommand {

    @Inject
    private CraftProtectPlugin plugin;

    @Inject
    private PlaytimeController controller;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.setplaytime";
    }

    @Override
    public String getUsage() {
        return "/<command> <player> [time in seconds]";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        if (args.length < 1)
            return false;

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            plugin.sendMessage(sender, M.NO_PERM);
            return true;
        }

        if (args.length == 1) {
            controller.updatePlaytime(target, aLong -> 0L);
            return true;
        }

        int time;
        try {
            time = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number");
            return true;
        }

        controller.updatePlaytime(target, playtime -> time * 1000L);
        return true;
    }
}
