package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.config.ChatConfig;
import com.github.oasis.craftprotect.config.CraftProtectConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.Map;

@Singleton
public class EmojiFeature implements Feature, Listener {



    @Inject
    private CraftProtect plugin;

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        CraftProtectConfig craftProtectConfig = plugin.getCraftProtectConfig();
        ChatConfig chat = craftProtectConfig.getChat();
        for (Map.Entry<String, String> entry : chat.getReplacements().entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        event.setMessage(message);

    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String[] words = message.split(" ");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (config.contains("emotes." + word)) {
                String emote = config.getString("emotes." + word);
                words[i] = emote;
            }
        }

        message = String.join(" ", words);
        event.setMessage(message);
    }

    ObjectInputFilter.Config config = config.init(new File(getDataFolder(), "emotes.yml"), "emotes.yml");


    @Override
    public void close() throws IOException {

    }

}
