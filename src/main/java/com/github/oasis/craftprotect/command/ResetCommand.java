package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    private final CraftProtect plugin;

    public ResetCommand(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cNur Spieler können diesen Befehl ausführen.");
            return true;
        }

        player.setPlayerListName(player.getDisplayName());
        player.setGlowing(false);
        player.sendMessage(plugin.getPrefix() + "§3Alles wurde von dir zurückgesetzt");

        return true;
    }
}
