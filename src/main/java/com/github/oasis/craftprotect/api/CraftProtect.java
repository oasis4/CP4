package com.github.oasis.craftprotect.api;

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

    long getOnlineTime(@NotNull UUID uniqueId);

    default long getOnlineTime(@NotNull OfflinePlayer player) {
        return getOnlineTime(player.getUniqueId());
    }

    @NotNull
    Component getMessage(@NotNull String key, @NotNull Object... objects);

    @NotNull
    Configuration getUnformattedMessages();

    void sendMessage(@NotNull CommandSender sender, @NotNull String key, @NotNull Object... objects);

    @NotNull
    Map<String, String> getChatReplacements();

}
