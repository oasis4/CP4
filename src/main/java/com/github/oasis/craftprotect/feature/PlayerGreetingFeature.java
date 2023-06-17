package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.config.ChatConfig;
import com.github.oasis.craftprotect.config.CraftProtectConfig;
import com.github.oasis.craftprotect.utils.CircleUtils;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.Iterator;

@Singleton
public class PlayerGreetingFeature implements Feature {


    @Inject
    private CraftProtect plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        CraftProtectConfig craftProtectConfig = plugin.getCraftProtectConfig();
        ChatConfig chat = craftProtectConfig.getChat();
        player.addAdditionalChatCompletions(chat.getReplacements().keySet());
        player.addAdditionalChatCompletions(chat.getReplacements().values());
        if(!player.hasPlayedBefore()) player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 200.0, 0.5));
        player.setGlowing(false);
        player.sendTitle("§3Willkommen", "§fbei " + plugin.getName(), 1, 20 * 3, 20 * 2);
        double seconds = 3.0;

        new BukkitRunnable() {
            final long created = System.currentTimeMillis();
            final Location locationfire = player.getLocation().clone();

            final Iterator<Vector> iterator = Iterables.cycle(CircleUtils.getCircleLocations()).iterator();

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
