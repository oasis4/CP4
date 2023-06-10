package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.controller.PlaytimeController;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Singleton
public class PlaytimeCommand implements CraftProtectCommand, Listener {

    private final CraftProtectPlugin plugin;

    @Inject
    private PlaytimeController controller;

    @Inject
    public PlaytimeCommand(CraftProtectPlugin plugin) {
        this.plugin = plugin;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setMetadata("last-joined", new FixedMetadataValue(plugin, System.currentTimeMillis()));
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        controller.getPlaytime(player)
                .thenApply(playtime -> playtime + (System.currentTimeMillis() - lastJoinedAt(player)))
                .thenAccept(playtime -> plugin.sendMessage(sender, "command.playtime.duration", DurationFormatUtils.formatDuration(playtime, "HH:mm:ss")));
        return true;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setMetadata("last-joined", new FixedMetadataValue(plugin, System.currentTimeMillis()));
        System.out.println("Joining");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        controller.updatePlaytime(player, playtime -> playtime + (System.currentTimeMillis() - lastJoinedAt(player)));
        player.removeMetadata("last-joined", plugin);
    }

    private long lastJoinedAt(Player player) {
        List<MetadataValue> list = player.getMetadata("last-joined");
        long lastJoined = System.currentTimeMillis();
        if (!list.isEmpty()) {
            lastJoined = list.get(0).asLong();
        }
        return lastJoined;
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (!plugin.equals(event.getPlugin()))
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            controller.updatePlaytime(player, playtime -> playtime + (System.currentTimeMillis() - lastJoinedAt(player)));
            player.removeMetadata("last-joined", plugin);
        }
    }


}