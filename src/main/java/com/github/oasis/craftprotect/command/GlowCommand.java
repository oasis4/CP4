package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class GlowCommand implements CommandExecutor {

    private final CraftProtect plugin;

    public GlowCommand(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender.hasPermission("cp4.sub"))) {
            sender.sendMessage(plugin.getPrefix() + "§3Du musst Sub bei Oasis4_0 oder Oreocast sein");
            return true;
        }

        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, "default.no-player");
            return true;
        }

        Location location = player.getLocation();

        player.setGlowing(!player.isGlowing());

        if (player.isGlowing()) {
            player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            plugin.sendMessage(player, "command.glow.enabled");
        } else {
            player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.2f);
            plugin.sendMessage(player, "command.glow.disabled");
        }
        return true;
    }
}






