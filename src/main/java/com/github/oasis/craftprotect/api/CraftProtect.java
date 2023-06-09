package com.github.oasis.craftprotect.api;

import com.github.oasis.craftprotect.link.Execution;
import com.github.oasis.craftprotect.storage.AsyncUserStorage;
import com.google.common.cache.Cache;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface CraftProtect extends Plugin {

    Component getPrefix();

    Component getFullPrefix();

    Component getMessageOfTheDay();

    @NotNull
    String getVersion();

    @Nullable
    Location getSpawnLocation();

    @NotNull
    Closeable attachRepeaterTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay, int period);

    @NotNull
    Closeable attachDelayedTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay);

    @NotNull
    Closeable attachAsyncRepeaterTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay, int period);

    @NotNull
    Closeable attachAsyncDelayedTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay);

    @Nullable
    Closeable getTask(@NotNull Player player, @NotNull String id);

    default void closeTask(@NotNull Player player, @NotNull String id) {
        Closeable task = getTask(player, id);
        if (task == null) return;
        try {
            task.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long getUptime(@NotNull UUID uniqueId);

    default long getUptime(@NotNull OfflinePlayer player) {
        return getUptime(player.getUniqueId());
    }

    @NotNull
    Component getMessage(@NotNull String key, @NotNull Object... objects);

    @NotNull
    Configuration getUnformattedMessages();

    void sendMessage(@NotNull CommandSender sender, @NotNull String key, @NotNull Object... objects);

    @NotNull
    Map<String, String> getChatReplacements();

    AsyncUserStorage getUserStorage();

    Cache<String, Execution> getAuthorizationCache();

    BukkitAudiences getAudiences();
}
