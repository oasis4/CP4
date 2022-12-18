package com.github.oasis.craftprotect;

import de.lebaasti.craftprotect4.functions.PlayerFunctionsKt;
import de.lebaasti.craftprotect4.functions.Status;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StreamerCommands implements CommandExecutor {

    private final CraftProtectPlugin plugin;

    public StreamerCommands(CraftProtectPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cDu musst ein Spieler sein.");
            return true;
        }
        if (!sender.hasPermission("cp4.streamer")) {
            sender.sendMessage(plugin.getPrefix() + "§cDir fehlen Berechtigungen um dies zutun.");
            return true;
        }

        PlayerFunctionsKt.setStatus(player, Status.LIVE);
        return true;
    }


}