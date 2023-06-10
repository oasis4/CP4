package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.SpawnController;
import com.github.oasis.craftprotect.utils.CircleUtils;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.*;
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
        if (controller.getLocation() == null || !event.hasChangedBlock())
            return;


        Player player = event.getPlayer();
        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() != Material.STRUCTURE_BLOCK) {
            BukkitTask remove = map.remove(player);
            if (remove != null)
                remove.cancel();
            return;
        }
        map.computeIfAbsent(player, player1 -> new BukkitRunnable() {
            private final long start = System.currentTimeMillis();
            final Iterator<Vector> iterator = Iterables.cycle(CircleUtils.getCircleLocations()).iterator();

            @Override
            public void run() {

                float progress = (System.currentTimeMillis() - start) / 5000f;

                if (progress >= 1) {
                    Bukkit.getScheduler().runTask(plugin, () -> player1.teleport(controller.getLocation()));
                    cancel();
                    map.remove(player);
                    return;
                }
                Location location = player1.getLocation().getBlock().getLocation().clone();
                location.add(0.5, 0.5, 0.5);

                for (int i = 0; i < CircleUtils.CIRCLE_SEGMENTS; i++) {
                    Color color = (float) i / CircleUtils.CIRCLE_SEGMENTS <= progress ? Color.GREEN : Color.RED;
                    Particle.DustOptions options = new Particle.DustOptions(color, 1);

                    Location add = location.clone().add(iterator.next());
                    player1.getWorld().spawnParticle(Particle.REDSTONE, add, 1, 0, 0, 0, 0, options);
                }

            }
        }.runTaskTimerAsynchronously(plugin, 0, 5));
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