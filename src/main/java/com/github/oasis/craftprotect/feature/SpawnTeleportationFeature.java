package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.SpawnController;
import com.github.oasis.craftprotect.utils.CircleUtils;
import com.github.oasis.craftprotect.utils.MoveUtils;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

@Singleton
public class SpawnTeleportationFeature implements Feature {

    @Inject
    private CraftProtectPlugin plugin;

    @Inject
    private SpawnController controller;

    private final Map<Player, BukkitTask> map = new WeakHashMap<>();

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (controller.getLocation() == null || !MoveUtils.movedBlock(event.getFrom(), event.getTo()))
            return;


        Player player = event.getPlayer();
        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() != Material.STRUCTURE_BLOCK) {
            BukkitTask remove = map.remove(player);
            if (remove != null)
                remove.cancel();
            return;
        }
        if (!player.isOnGround())
            return;
        map.computeIfAbsent(player, player1 -> new BukkitRunnable() {
            private final long start = System.currentTimeMillis();
            final Iterator<Vector> iterator = Iterables.cycle(CircleUtils.getCircleLocations()).iterator();

            @Override
            public void run() {

                if (System.currentTimeMillis() - start > 5000) {
                    Bukkit.getScheduler().runTask(plugin, () -> player1.teleport(controller.getLocation()));
                    cancel();
                    map.remove(player);
                    return;
                }

                Location location = player1.getLocation().getBlock().getLocation().clone();
                location.add(0.5, 0.5, 0.5);
                Location add = location.add(iterator.next());
                player1.getWorld().spawnParticle(Particle.FLAME, add, 1, 0, 0, 0, 0);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BukkitTask remove = this.map.remove(event.getPlayer());
        if (remove != null)
            remove.cancel();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        BukkitTask remove = this.map.remove(event.getEntity());
        if (remove != null)
            remove.cancel();
    }

    @Override
    public void close() throws IOException {

    }
}