package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.Feature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nullable;
import java.io.IOException;

@Singleton
public class ChatFeature implements Feature {

    @Inject
    @Nullable
    private Chat chat;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (chat == null)
            return;

        String playerPrefix = chat.getPlayerPrefix(player);
        String playerSuffix = chat.getPlayerSuffix(player);
        String messagePrefix = chat.getPlayerInfoString(player, "message-prefix", "");
        event.setFormat(ChatColor.translateAlternateColorCodes('&', playerPrefix + "%s" + playerSuffix + " §8»§r " + messagePrefix + "%s"));
    }

    @Override
    public void close() throws IOException {

    }
}
