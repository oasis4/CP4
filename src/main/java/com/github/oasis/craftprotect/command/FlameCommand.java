package com.github.oasis.craftprotect.command;


import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

public class FlameCommand implements CraftProtectCommand {


    public final CraftProtect plugin;

    public FlameCommand(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;

        Closeable flame = plugin.getTask(player, "flame");
        if (flame != null) {
            try {
                flame.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        plugin.attachAsyncRepeaterTask(player, "flame", () -> {
            World world = player.getWorld();
            Location location = player.getLocation();
            world.spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0);
        }, 0, 1);
        return true;
    }

}













