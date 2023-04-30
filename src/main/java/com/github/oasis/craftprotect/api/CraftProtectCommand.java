package com.github.oasis.craftprotect.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CraftProtectCommand extends CommandExecutor, TabCompleter {

    default @Nullable String getPermission() {
        return null;
    }

    default @Nullable String getPermissionMessage() {
        return null;
    }

    @Nullable
    @Override
    default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
