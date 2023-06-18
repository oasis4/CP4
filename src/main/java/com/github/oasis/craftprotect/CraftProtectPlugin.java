package com.github.oasis.craftprotect;

import com.github.oasis.craftprotect.adventure.MessageArgumentResolver;
import com.github.oasis.craftprotect.adventure.MessageResolver;
import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.api.FeaturedPlugin;
import com.github.oasis.craftprotect.command.*;
import com.github.oasis.craftprotect.config.CraftProtectConfig;
import com.github.oasis.craftprotect.config.SQLDatabaseConfig;
import com.github.oasis.craftprotect.feature.*;
import com.github.oasis.craftprotect.link.Execution;
import com.github.oasis.craftprotect.storage.AsyncUserStorage;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.net.httpserver.HttpServer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;


public final class CraftProtectPlugin extends FeaturedPlugin implements CraftProtect, Listener {

    private Configuration messages;

    private final Table<Player, String, Closeable> schedulerTable = HashBasedTable.create();

    private BukkitAudiences audiences;

    private AsyncUserStorage userStorage;

    private String twitchAuthorizeURL;
    private HttpServer httpServer;

    private final Cache<String, Execution> authorizationCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofSeconds(30)).weakValues().build();
    private CraftProtectConfig craftProtectConfig;

    @Override
    public void onEnable() {
        super.onEnable();
        this.audiences = BukkitAudiences.create(this);

        getLogger().info("Initializing configuration files...");
        saveDefaultConfig();
        reloadCraftProtectConfig();

        SQLDatabaseConfig database = getCraftProtectConfig().getDatabase();
        if (database != null && database.isEnabled()) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                this.userStorage = new AsyncUserStorage(() -> {
                    try {
                        return DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            httpServer = HttpServer.create(new InetSocketAddress(3000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.injector = newInjector();

        reloadCraftProtectConfig();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        setupMessages(messagesFile);

        loadFeature(EmojiFeature.class);
        loadFeature(SpawnElytraFeature.class);
        loadFeature(PlayerGreetingFeature.class);
        loadFeature(GroupFeature.class);
        loadFeature(PlayerWingsFeature.class);
        loadFeature(CrystalFeature.class);
        loadFeature(CreeperFeature.class);
        loadFeature(SpawnTeleportationFeature.class);
        loadFeature(PlayerDisplayFeature.class);
        loadFeature(SpawnFeature.class);
        loadFeature(MotdFeature.class);
        loadFeature(DisableEndFeature.class);

        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            loadFeature(ChatFeature.class);
        }

        if (this.userStorage != null) loadFeature(LiveStreamFeature.class);

        loadFeature(AfkFeature.class);
        registerCommand(AfkCommand.class, "afk");

        registerCommand(ClearItemCommand.class, "clear");

        registerCommand(GlowCommand.class, "glow");
        registerCommand(ResetCommand.class, "reset");
        registerCommand(FlameCommand.class, "flame");
        registerCommand(LiveCommand.class, "live");
        registerCommand(OasisLiveCommand.class, "Oasislive");
        registerCommand(TanjoCommand.class, "tanjo");
        registerCommand(SpawnFireworkCommand.class, "firework");
        registerCommand(LinkCommand.class, "link");
        registerCommand(PlaytimeCommand.class, "playtime");
        registerCommand(SetPlaytimeCommand.class, "setplaytime");
        registerCommand(SetSpawnCommand.class, "setspawn");
        registerCommand(SpawnCommand.class, "spawn");
        registerCommand(CPCommand.class, "craftprotect");

        if (userStorage != null) registerCommand(UserCommand.class, "user");

        if (httpServer != null) httpServer.start();

    }

    public void reloadCraftProtectConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.isFile()) {
            try (FileReader reader = new FileReader(configFile, StandardCharsets.UTF_8)) {
                Yaml yaml = new Yaml();
                this.craftProtectConfig = yaml.loadAs(reader, CraftProtectConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Injector newInjector() {
        return Guice.createInjector(Stage.PRODUCTION, new CraftProtectInjectModule(this));
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    private void setupMessages(File messagesFile) {
        saveResource(messagesFile.getName(), false);

        try (FileReader reader = new FileReader(messagesFile, StandardCharsets.UTF_8)) {
            messages = YamlConfiguration.loadConfiguration(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Reader reader = getTextResource(messagesFile.getName())) {
            messages.setDefaults(YamlConfiguration.loadConfiguration(reader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void registerCommand(Class<? extends CraftProtectCommand> executorClass, String name, String... aliases) {
        CraftProtectCommand instance = super.injector.getInstance(executorClass);
        if (instance == null) {
            getLogger().warning("Failed to register command with name \"" + name + "\"");
            return;
        }

        CommandMap commandMap = Bukkit.getCommandMap();
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            if (!constructor.trySetAccessible())
                throw new IllegalStateException();
            PluginCommand command = constructor.newInstance(name, this);

            command.setUsage(instance.getUsage());
            command.setDescription(instance.getDescription());
            command.setExecutor(instance);
            command.setTabCompleter(instance);
            command.setPermission(instance.getPermission());
            command.setPermissionMessage(instance.getPermissionMessage());
            command.setAliases(Arrays.asList(aliases));

            commandMap.register(name, getName(), command);

            if (instance instanceof Listener listener) {
                Bukkit.getPluginManager().registerEvents(listener, this);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (httpServer != null) {
            getLogger().info("Stopping http server on port 3000...");
            httpServer.stop(0);
        }

        HandlerList.unregisterAll((Plugin) this);
    }

    public CraftProtectConfig getCraftProtectConfig() {
        return craftProtectConfig;
    }

    @Override
    public Component getPrefix() {
        return getMessage("prefix");
    }

    @Override
    public Component getFullPrefix() {
        return getMessage("full-prefix");
    }

    @NotNull
    @Override
    public String getVersion() {
        return getDescription().getVersion();
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
            try {
                entry.getValue().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public String getTwitchAuthorizeURL() {
        return twitchAuthorizeURL;
    }

    @Override
    public void sendMessage(@NotNull CommandSender sender, @NotNull String key, @NotNull Object... objects) {
        audiences.sender(sender).sendMessage(getMessage(key, objects));
    }

    @Override
    public AsyncUserStorage getUserStorage() {
        return userStorage;
    }

    @Override
    public Cache<String, Execution> getAuthorizationCache() {
        return authorizationCache;
    }

    @Override
    public BukkitAudiences getAudiences() {
        return audiences;
    }
}

