package com.github.oasis.craftprotect;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.command.*;
import com.github.oasis.craftprotect.feature.*;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


public final class CraftProtectPlugin extends JavaPlugin implements CraftProtect, Listener {

    private File userDataFolder;

    private Configuration messages;

    private Map<String, String> chatReplacements;

    private final Table<Player, String, Closeable> schedulerTable = HashBasedTable.create();

    private Location spawnLocation;

    private BukkitAudiences audiences;

    @Override
    public void sendMessage(@NotNull CommandSender sender, @NotNull String key, @NotNull Object... objects) {
        audiences.sender(sender).sendMessage(getMessage(key, objects));
    }

    @Override
    public void onEnable() {

        this.audiences = BukkitAudiences.create(this);

        getLogger().info("Initializing configuration files...");
        saveDefaultConfig();
        reloadConfig();

        chatReplacements = loadChatReplacements();

        userDataFolder = new File(getDataFolder(), "userdata");
        if (!userDataFolder.isDirectory()) userDataFolder.mkdirs();

        Reader textResource = getTextResource("messages.yml");
        if (textResource == null) {
            throw new RuntimeException("Missing messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(textResource);

        UptimeCommand uptimeCommand = new UptimeCommand(this);
        registerCommand("uptime", uptimeCommand);
        getServer().getPluginManager().registerEvents(uptimeCommand, this);

        AFKRankFeature afkRankFeature = new AFKRankFeature();
        registerCommand("afk", afkRankFeature);
        getServer().getPluginManager().registerEvents(afkRankFeature, this);

        // TODO: Change this
        spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();

        registerCommand("sub", new GlowCommand(this));
        registerCommand("reset", new ResetCommand(this));
        registerCommand("flame", new FlameCommand(this));
        registerCommand("live", new LiveCommand(this));
        registerCommand("Oasislive", new OasisLiveCommand(this));
        registerCommand("tanjo", new TanjoCommand(this));
        registerCommand("rw", new SpawnFireworkCommand(this));


        Bukkit.getPluginManager().registerEvents(new EmojiFeature(this), this);
        Bukkit.getPluginManager().registerEvents(new SpawnElytraFeature(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerGreetingFeature(this), this);
        Bukkit.getPluginManager().registerEvents(new Teleportation(this), this);
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    private Map<String, String> loadChatReplacements() {
        ConfigurationSection section = getConfig().getConfigurationSection("chat.replacements");
        if (section == null) {
            return Collections.emptyMap();
        }
        Set<String> keys = section.getKeys(false);
        Map<String, String> replacements = new HashMap<>(keys.size());
        for (String key : keys) {
            String replacement = section.getString(key);
            if (replacement == null) {
                continue;
            }
            replacements.put(key, ChatColor.translateAlternateColorCodes('&', replacement));
        }
        return replacements;
    }

    private boolean registerCommand(String name, CraftProtectCommand executor) {
        PluginCommand command = getCommand(name);
        if (command == null) return false;

        command.setExecutor(executor);

        if (executor instanceof TabCompleter) {
            command.setExecutor(executor);
        }

        command.setPermission(executor.getPermission());
        command.setPermissionMessage(executor.getPermissionMessage());
        return true;
    }

    @Override
    public void onDisable() {
        this.getLogger().info("ENDE");

    }

    public File getUserDataFolder() {
        return this.userDataFolder;
    }


    @Override
    public Component getPrefix() {
        return getMessage("prefix");
    }

    @NotNull
    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Nullable
    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @NotNull
    @Override
    public Closeable attachRepeaterTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay, int period) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(this, task, delay, period);
        Closeable closeable = () -> {
            bukkitTask.cancel();
            schedulerTable.remove(player, id);
        };
        schedulerTable.put(player, id, closeable);
        return closeable;
    }

    @NotNull
    @Override
    public Closeable attachDelayedTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(this, task, delay);
        Closeable closeable = () -> {
            bukkitTask.cancel();
            schedulerTable.remove(player, id);
        };
        schedulerTable.put(player, id, closeable);
        return closeable;
    }

    @NotNull
    @Override
    public Closeable attachAsyncRepeaterTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay, int period) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, task, delay, period);
        Closeable closeable = () -> {
            bukkitTask.cancel();
            schedulerTable.remove(player, id);
        };
        schedulerTable.put(player, id, closeable);
        return closeable;
    }

    @NotNull
    @Override
    public Closeable attachAsyncDelayedTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task, int delay) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(this, task, delay);
        Closeable closeable = () -> {
            bukkitTask.cancel();
            schedulerTable.remove(player, id);
        };
        schedulerTable.put(player, id, closeable);
        return closeable;
    }

    @NotNull
    @Override
    public Closeable getTask(@NotNull Player player, @NotNull String id) {
        return schedulerTable.get(player, id);
    }

    @EventHandler
    public void stopTasks(PlayerQuitEvent event) {
        Map<String, Closeable> row = this.schedulerTable.row(event.getPlayer());
        for (Map.Entry<String, Closeable> entry : row.entrySet()) {
            System.out.println("Closing " + entry.getKey() + "...");

            try {
                entry.getValue().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getUptime(@NotNull UUID uniqueId) {
        File userDataFolder = getUserDataFolder();
        File userData = new File(userDataFolder, "%s.yml".formatted(uniqueId));
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        return configuration.getLong("online-time", 0L);
    }

    private final TagResolver DEFAULT = TagResolver.builder().resolvers(new MessageResolver(this), TagResolver.standard()).build();

    @NotNull
    @Override
    public Component getMessage(@NotNull String key, @NotNull Object... objects) {
        String message = messages.getString(key);
        if (message == null) {
            return Component.text("Missing message <%s> %s".formatted(key, Arrays.toString(objects))).color(NamedTextColor.RED);
        }

        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(message, TagResolver.resolver(DEFAULT, new MessageArgumentResolver(objects)));
    }

    @NotNull
    @Override
    public Configuration getUnformattedMessages() {
        return this.messages;
    }

    @NotNull
    @Override
    public Map<String, String> getChatReplacements() {
        if (chatReplacements == null) {
            return Collections.emptyMap();
        }
        return chatReplacements;
    }
}

