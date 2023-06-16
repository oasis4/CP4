package com.github.oasis.craftprotect.command;


import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

@Singleton
public class FlameCommand implements CraftProtectCommand {

    private static final String taskName = "flame";

    @Inject
    public CraftProtect plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        Closeable flame = plugin.getTask(player, taskName);
        if (flame != null) {
            try {
                flame.close();
                plugin.sendMessage(player, "command.flame.disabled");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        plugin.attachAsyncRepeaterTask(player, taskName, () -> {
            World world = player.getWorld();
            Location location = player.getLocation();
            world.spawnParticle(Particle.CHERRY_LEAVES, location, 1, 0, 0, 0, 0);
        }, 0, 1);
        plugin.sendMessage(player, "command.flame.enabled");
        return true;
    }

}













