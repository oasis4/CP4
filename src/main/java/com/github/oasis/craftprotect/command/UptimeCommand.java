package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class UptimeCommand implements CraftProtectCommand, Listener {

    private final CraftProtectPlugin plugin;

    public UptimeCommand(CraftProtectPlugin plugin) {
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

        File userDataFolder = plugin.getUserDataFolder();
        File userData = new File(userDataFolder, player.getUniqueId() + ".yml");

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        long uptime = configuration.getLong("uptime", 0L);


        List<MetadataValue> list = player.getMetadata("last-joined");
        long lastJoined = 0;
        if (!list.isEmpty()) {
            lastJoined = list.get(0).asLong();
        }
        uptime += (System.currentTimeMillis() - lastJoined);

        plugin.sendMessage(sender, "command.uptime.duration", DurationFormatUtils.formatDuration(uptime, "HH:mm:ss"));

        return true;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setMetadata("last-joined", new FixedMetadataValue(plugin, System.currentTimeMillis()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayer(player);
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (!plugin.equals(event.getPlugin()))
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayer(player);
        }
    }

    private void savePlayer(Player player) {
        File userDataFolder = plugin.getUserDataFolder();
        File userData = new File(userDataFolder, player.getUniqueId() + ".yml");

        if (!userData.isFile()) {
            try {
                userData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        long onlineTime = configuration.getLong("uptime", 0L);

        List<MetadataValue> list = player.getMetadata("last-joined");
        long lastJoined = 0;
        if (!list.isEmpty()) {
            lastJoined = list.get(0).asLong();
        }

        onlineTime += (System.currentTimeMillis() - lastJoined);

        configuration.set("uptime", onlineTime);
        try {
            configuration.save(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.removeMetadata("last-joined", plugin);
    }


}