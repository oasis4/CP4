package com.github.oasis.craftprotect.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface CraftProtectCommand extends CommandExecutor, TabCompleter {

    default String getUsage() {
        return "/<command>";
    }

    default @Nullable String getDescription() {
        return null;
    }

    default @Nullable String getPermission() {
        return null;
    }

    default @Nullable String getPermissionMessage() {
        return null;
    }

    default @Nullable Stream<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    @Nullable
    @Override
    default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Stream<String> stringStream = tabComplete(sender, command, label, args);
        if (stringStream == null)
            return null;
        return StringUtil.copyPartialMatches(args[args.length - 1], stringStream.toList(), new ArrayList<>());
    }
}
