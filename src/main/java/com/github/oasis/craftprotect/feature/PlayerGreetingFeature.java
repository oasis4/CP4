package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerGreetingFeature implements Listener {

    private final CraftProtect plugin;

    public PlayerGreetingFeature(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        Location locationfire = player.getLocation().clone();
        player.setGlowing(false);
        player.sendTitle("§3Willkommen", "§fbei " + plugin.getName(), 1, 20, 1);

        e.setJoinMessage("§8[§b+§8] " + player.getDisplayName());


        double seconds = 3.0;
        double segments = 16;
        double radius = 1;

        new BukkitRunnable() {
            final long created = System.currentTimeMillis();

            @Override
            public void run() {
                if ((created + seconds * 1000) < System.currentTimeMillis()) {
                    cancel();
                    return;
                }
                for (double pa = 0.0; pa < 2 * Math.PI; pa += 2 * Math.PI / segments) { // TODO: Use another algorithm
                    Location l = locationfire.clone().add(
                            Math.cos(pa) * radius, // X
                            0.3, // Y
                            Math.sin(pa) * radius // Z
                    );
                    world.spawnParticle(Particle.FLAME, l, 1, 0, 0, 0, 0);
                    Location l2 = l.clone().add(0, 0.4, 0);
                    world.spawnParticle(Particle.FLAME, l2, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 10, 10);

    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§7" + e.getPlayer().getDisplayName() + " hat das Spiel verlassen!");
    }
}
