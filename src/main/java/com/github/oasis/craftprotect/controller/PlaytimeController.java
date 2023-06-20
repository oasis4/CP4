package com.github.oasis.craftprotect.controller;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Singleton
public class PlaytimeController {

    private final File userDataFolder;
    private final LoadingCache<OfflinePlayer, Long> playtimeCache = CacheBuilder.newBuilder()
            .weakKeys()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull Long load(@NotNull OfflinePlayer player) {
                    return loadPlaytime(player.getUniqueId());
                }
            });

    @Inject
    private PlayerDisplayController displayController;

    @Inject
    public PlaytimeController(CraftProtectPlugin plugin) {
        userDataFolder = new File(plugin.getDataFolder(), "userdata");
        if (!userDataFolder.isDirectory()) userDataFolder.mkdirs();
    }

    public CompletableFuture<Long> getPlaytime(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return playtimeCache.get(player);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return 0L;
        });
    }

    public void updatePlaytime(OfflinePlayer player, Function<Long, Long> consumer) {
        getPlaytime(player)
                .thenAccept(time -> {
                    time = consumer.apply(time);
                    playtimeCache.put(player, time);
                    try {
                        savePlaytime(player.getUniqueId(), time);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (player.isOnline())
                        displayController.updateGroup(player.getPlayer(), time);
                });
    }

    public long loadPlaytime(@NotNull UUID uniqueId) {
        File userData = new File(userDataFolder, "%s.yml".formatted(uniqueId));
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        return configuration.getLong("play-time", 0L);
    }

    public void savePlaytime(@NotNull UUID id, long time) throws IOException {
        File userData = new File(userDataFolder, "%s.yml".formatted(id));
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("play-time", time);
        configuration.save(userData);
    }


}
