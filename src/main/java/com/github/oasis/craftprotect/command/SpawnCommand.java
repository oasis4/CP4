package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.controller.SpawnController;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnCommand implements CraftProtectCommand {

    @Inject
    private CraftProtect protect;

    @Inject
    private SpawnController controller;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.spawn";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            protect.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        if (controller.getLocation() == null) {
            protect.sendMessage(sender, "spawn.missing");
            return true;
        }

        player.teleport(controller.getLocation());
        protect.sendMessage(sender, "command.spawn.successful");
        return true;
    }
}
