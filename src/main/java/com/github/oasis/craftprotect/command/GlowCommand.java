package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@Singleton
public class GlowCommand implements CraftProtectCommand {

    @Inject
    private CraftProtect plugin;

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender.hasPermission("cp4.sub"))) {
            plugin.sendMessage(sender, M.NO_SUB);
            return true;
        }

        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
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






