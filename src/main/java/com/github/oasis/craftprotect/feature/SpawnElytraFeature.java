package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.SpawnController;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class SpawnElytraFeature implements Feature {
    private static final int radius = 40;
    private static final int boostMultiplier = 3;

    private final List<Player> flyingPlayers = new ArrayList<>();
    private final List<Player> boostedPlayers = new ArrayList<>();
    @Inject
    private SpawnController controller;

    @Inject
    public SpawnElytraFeature(CraftProtectPlugin protect) {
        Bukkit.getScheduler().runTaskTimer(protect, () -> {
            Collection<? extends Player> players = (controller.getLocation() != null) ? controller.getLocation().getWorld().getPlayers() : Bukkit.getOnlinePlayers();
            players.forEach(player -> {
                if (player.getGameMode() != GameMode.SURVIVAL) return;
                player.setAllowFlight(isInSpawnRadius(player));
                if (flyingPlayers.contains(player) && !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.setGliding(false);
                    Bukkit.getScheduler().runTaskLater(protect, () -> {
                        flyingPlayers.remove(player);
                        boostedPlayers.remove(player);
                    }, 5);
                }
            });
        }, 0, 5);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) return;
        if (!isInSpawnRadius(player)) return;
        event.setCancelled(true);
        player.setGliding(true);
        flyingPlayers.add(player);
        player.sendActionBar(Component.text("Drücke ").append(Component.keybind("key.swapOffhand")).append(Component.text(", um dich zu boosten")));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!flyingPlayers.contains(player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL || event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (flyingPlayers.contains(event.getPlayer()) && event.getPlayer().isGliding() && !boostedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(boostMultiplier));
            boostedPlayers.add(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player player && flyingPlayers.contains(player)) event.setCancelled(true);
    }

    private boolean isInSpawnRadius(Player player) {
        if (controller.getLocation() == null) return false;
        if (player.getLocation().getWorld() != controller.getLocation().getWorld()) return false;
        return controller.getLocation().distance(player.getLocation()) <= radius;
    }

    @Override
    public void close() throws IOException {

    }
}
