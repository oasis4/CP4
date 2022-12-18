package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class EmojiFeature implements Listener {

    private final CraftProtect plugin;

    public EmojiFeature(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        for (Map.Entry<String, String> entry : plugin.getChatReplacements().entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
    }

}
