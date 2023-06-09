package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.Feature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.Map;

@Singleton
public class EmojiFeature implements Feature {

    @Inject
    private CraftProtect plugin;

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
