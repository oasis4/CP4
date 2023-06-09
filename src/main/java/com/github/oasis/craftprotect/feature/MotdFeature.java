package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.Feature;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class MotdFeature implements Feature<CraftProtectPlugin> {

    private CraftProtectPlugin plugin;

    @Override
    public void init(CraftProtectPlugin plugin) throws IOException {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Component messageOfTheDay = plugin.getMessageOfTheDay();
        if (messageOfTheDay != null) {
            plugin.getAudiences().sender(player).sendMessage(messageOfTheDay);
        }
    }

    @Override
    public void close() throws IOException {

    }
}
