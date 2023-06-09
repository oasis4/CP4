package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.Feature;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Singleton
public class PlayerGreetingFeature implements Feature {

    private static final int CIRCLE_SEGMENTS = 16;
    private static final int CIRCLE_RADIUS = 1;

    @Inject
    private CraftProtect plugin;
    private final List<Vector> circleLocations = new ArrayList<>(CIRCLE_SEGMENTS);

    public PlayerGreetingFeature() {
        // Precalculate the circle
        for (double pa = 0.0; pa < 2 * Math.PI; pa += 2 * Math.PI / CIRCLE_SEGMENTS) {
            this.circleLocations.add(new Vector(Math.cos(pa) * CIRCLE_RADIUS, 0, Math.sin(pa) * CIRCLE_RADIUS));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        player.setGlowing(false);
        player.sendTitle("§3Willkommen", "§fbei " + plugin.getName(), 1, 20 * 3, 20 * 2);

        double seconds = 3.0;

        new BukkitRunnable() {
            final long created = System.currentTimeMillis();
            final Location locationfire = player.getLocation().clone();

            final Iterator<Vector> iterator = Iterables.cycle(circleLocations).iterator();

            @Override
            public void run() {
                if ((created + seconds * 1000) < System.currentTimeMillis()) {
                    cancel();
                    return;
                }

                Vector next = iterator.next();
                Location loc = locationfire.clone().add(next);
                world.spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0);

                locationfire.add(0, 0.05, 0);
            }
        }.runTaskTimerAsynchronously(plugin, 1, 1);

    }

    @Override
    public void close() throws IOException {

    }
}
