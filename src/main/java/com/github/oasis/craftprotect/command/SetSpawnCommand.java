package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.controller.SpawnController;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class SetSpawnCommand implements CraftProtectCommand {

    @Inject
    private CraftProtect protect;

    @Inject
    private SpawnController controller;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.setspawn";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            protect.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        Location location = player.getLocation();
        try {
            controller.setLocation(location);
            location.getWorld().setSpawnLocation(location);
            protect.sendMessage(sender, "command.setspawn.successful");
        } catch (IOException e) {
            e.printStackTrace();
            protect.sendMessage(sender, "command.setspawn.fail");
        }

        return true;
    }
}
