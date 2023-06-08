package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Map;

public class EmojiFeature implements Feature<CraftProtectPlugin> {

    private CraftProtect plugin;

    @Override
    public void init(CraftProtectPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        for (Map.Entry<String, String> entry : plugin.getChatReplacements().entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void close() throws IOException {

    }

}
