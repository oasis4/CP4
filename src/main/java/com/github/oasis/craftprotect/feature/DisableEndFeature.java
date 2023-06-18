package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.Feature;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;

public class DisableEndFeature implements Feature {

    @Inject
    private CraftProtectPlugin plugin;

    @EventHandler
    public void onDisableEnd(PlayerTeleportEvent event) {
        if (!"world_the_end".equalsIgnoreCase(event.getTo().getWorld().getName()))
            return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        plugin.sendMessage(player, "disabled-end");
    }


    @Override
    public void close() throws IOException {

    }
}
