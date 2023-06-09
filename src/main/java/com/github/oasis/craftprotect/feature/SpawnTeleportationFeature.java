package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.Feature;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;

@Singleton
public class SpawnTeleportationFeature implements Feature {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STRUCTURE_BLOCK) {
            player.teleport(new Location(player.getWorld(), 189, 100, -125));
        }

    }

    @Override
    public void close() throws IOException {

    }
}