package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class ResetCommand implements CraftProtectCommand {

    @Inject
    private CraftProtect plugin;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.reset";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }
        player.setGlowing(false);
        plugin.sendMessage(sender, "command.reset");

        return true;
    }
}
