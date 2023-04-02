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
        player.sendTitle("§3Willkommen", "§fbei " + plugin.getName(), 1, 20 * 3, 20*2);

        //e.setJoinMessage("§8[§b+§8] " + player.getDisplayName());


        double seconds = 3.0;
        double segments = 3;
        double radius = 1;

        locationfire.add(0, 0.4, 0);

        new BukkitRunnable() {
            final long created = System.currentTimeMillis();

            @Override
            public void run() {
                if ((created + seconds * 1000) < System.currentTimeMillis()) {
                    cancel();
                    return;
                }
                System.out.println("Spawn circle");

                double d = 16; // Kontrollvariable
                double x = 0;
                double z = radius;
                Location center = locationfire.clone();
//                do {
//                    double bX = center.getX();
//                    double bZ = center.getZ();
//
//                    // 7. Oktant
//                    if (bX + x >= 0 && bZ + z >= 0) {
//                        spawnParticle(center.clone().add(x, 0, z));
//                    }
//                    // 2. Oktant
//                    if (bX + x >= 0 && bZ - z >= 0) {
//                        spawnParticle(center.clone().add(x, 0, -z));
//                    }
//                    // 6. Oktant
//                    if (bX - x >= 0 && bZ + z >= 0) {
//                        spawnParticle(center.clone().add(-x, 0, z));
//                    }
//                    // 3. Oktant
//                    if (bX - x >= 0 && bZ - z >= 0) {
//                        spawnParticle(center.clone().add(-x, 0, -z));
//                    }
//                    // 8. Oktant
//                    if (bX + z >= 0 && bZ + x >= 0) {
//                        spawnParticle(center.clone().add(z, 0, x));
//                    }
//                    // 1. Oktant
//                    if (bX + z >= 0 && bZ - x >= 0) {
//                        spawnParticle(center.clone().add(z, 0, -x));
//                    }
//                    // 4. Oktant
//                    if (bX - z >= 0 && bZ + x >= 0) {
//                        spawnParticle(center.clone().add(-z, 0, x));
//                    }
//                    // 5. Oktant
//                    if (bX - z >= 0 && bZ - x >= 0) {
//                        spawnParticle(center.clone().add(-z, 0, -x));
//                    }
//
//                    if (d < 0) {
//                        d += 2 * x + 1;
//                    } else {
//                        d += 2 * (x - z) + 1;
//                        z -= 1 / segments;
//                    }
//                    x += 1.0 / segments;
//
//                } while (x <= z);


                // Very heavy calculation. Replace this with the midpoint calculation
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

    private void spawnParticle(Location location) {
        World world = location.getWorld();
        world.spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        //e.setQuitMessage("§7" + e.getPlayer().getDisplayName() + " hat das Spiel verlassen!");
    }
}
