package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.controller.MotdController;
import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CPCommand implements CraftProtectCommand {

    @Inject
    private MotdController motdController;

    @Inject
    private CraftProtectPlugin plugin;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.cp";
    }

    @Override
    public String getUsage() {
        return "/<command> <reload|reload-motd|motd>";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (args[0].equalsIgnoreCase("reload-motd")) {
            motdController.reloadMotd();
            return true;
        }

        if (args[0].equalsIgnoreCase("motd")) {
            sender.sendMessage(motdController.getMessageOfTheDay());
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadCraftProtectConfig();
            return true;
        }

        return false;
    }

    @Override
    public @Nullable Stream<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return Stream.of("reload", "motd", "reload-motd");
        }

        return CraftProtectCommand.super.tabComplete(sender, command, label, args);
    }
}
