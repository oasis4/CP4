package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.SpawnController;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.io.IOException;

public class SpawnFeature implements Feature {

    @Inject
    private CraftProtect protect;
    @Inject
    private SpawnController controller;

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (controller.getLocation() == null && player.isOp()) {
            protect.sendMessage(player, "spawn.missing");
            return;
        }
        // Respawn at the bed if the player has a valid location
        if (event.getPlayer().getBedSpawnLocation() != null)
            return;

        event.setRespawnLocation(controller.getLocation());
    }

    @EventHandler
    public void onFirstJoin(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        if (controller.getLocation() == null && player.isOp()) {
            protect.sendMessage(player, "spawn.missing");
            return;
        }
        if (player.hasPlayedBefore())
            return;
        event.setSpawnLocation(controller.getLocation());
    }

    @Override
    public void close() throws IOException {

    }
}
