package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class SetPlayTimeCommand implements CraftProtectCommand {

    @Inject
    private CraftProtectPlugin plugin;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.setplaytime";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        if (args.length == 0) {
            player.setStatistic(Statistic.TOTAL_WORLD_TIME, 0);
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        int time;
        try {
            time = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number");
            return true;
        }

        player.setStatistic(Statistic.TOTAL_WORLD_TIME, time * 20);
        return true;
    }
}
