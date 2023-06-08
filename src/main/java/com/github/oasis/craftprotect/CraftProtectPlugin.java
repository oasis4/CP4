package com.github.oasis.craftprotect;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.api.FeaturedPlugin;
import com.github.oasis.craftprotect.command.*;
import com.github.oasis.craftprotect.feature.*;
import com.github.oasis.craftprotect.link.*;
import com.github.oasis.craftprotect.storage.AsyncUserStorage;
import com.github.oasis.craftprotect.utils.PlayerDisplay;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sun.net.httpserver.HttpServer;
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
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Consumer;


public final class CraftProtectPlugin extends FeaturedPlugin implements CraftProtect, Listener {

    public static Executor MAIN_EXECUTOR;

    private File userDataFolder;

    private Configuration messages;

    private Map<String, String> chatReplacements;

    private final Table<Player, String, Closeable> schedulerTable = HashBasedTable.create();

    private Location spawnLocation;

    private BukkitAudiences audiences;

    private AsyncUserStorage userStorage;

    private String twitchAuthorizeURL;
    private HttpServer httpServer;

    private Map<Player, PlayerDisplay> displayMap = new WeakHashMap<>();

    private Cache<String, Execution> authorizationCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(30))
            .weakValues()
            .build();


    @Override
    public void onEnable() {
        super.onEnable();

        MAIN_EXECUTOR = command -> Bukkit.getScheduler().runTask(CraftProtectPlugin.this, command);

        this.audiences = BukkitAudiences.create(this);

        getLogger().info("Initializing configuration files...");
        saveDefaultConfig();
        reloadConfig();

        chatReplacements = loadChatReplacements();

        userDataFolder = new File(getDataFolder(), "userdata");
        if (!userDataFolder.isDirectory()) userDataFolder.mkdirs();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        setupMessages(messagesFile);

        PlaytimeCommand playtimeCommand = new PlaytimeCommand(this);
        registerCommand("playtime", playtimeCommand);
        getServer().getPluginManager().registerEvents(playtimeCommand, this);

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
        registerCommand("rw", new SpawnFireworkCommand(this));
        registerCommand("link", new LinkCommand(this));


        loadFeature(new EmojiFeature());
        loadFeature(new SpawnElytraFeature());
        loadFeature(new PlayerGreetingFeature());
        loadFeature(new GroupFeature());

        ConfigurationSection database = getConfig().getConfigurationSection("database");
        if (database != null) {
            this.userStorage = new AsyncUserStorage(() -> {
                try {
                    return DriverManager.getConnection(database.getString("url"), database.getString("username"), database.getString("password"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }
        try {
            httpServer = HttpServer.create(new InetSocketAddress(3000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigurationSection twitchSection = getConfig().getConfigurationSection("twitch");
        if (twitchSection != null && httpServer != null) {
            String clientId = twitchSection.getString("client-id");
            String clientSecret = twitchSection.getString("client-secret");
            String callbackURI = twitchSection.getString("callback-uri", "localhost:3000");
            twitchAuthorizeURL = "https://id.twitch.tv/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s&state={sessionId}".formatted(clientId, callbackURI, "code", "");

            LiveStreamFeature feature = new LiveStreamFeature(clientId, clientSecret);
            loadFeature(feature);

            httpServer.createContext("/twitch", new TwitchLinkHandler(this, new TwitchClientInfo(clientId, clientSecret, callbackURI), feature));
        }


        ConfigurationSection minecraftSection = getConfig().getConfigurationSection("minecraft");
        if (minecraftSection != null && httpServer != null) {
            String clientId = minecraftSection.getString("client-id");
            //String clientSecret = twitchSection.getString("client-secret");
            String callbackURI = minecraftSection.getString("callback-uri", "localhost:3000");
            twitchAuthorizeURL = """
                    https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?
                    client_id=%s
                    &response_type=token
                    &redirect_uri=%s
                    &scope=XboxLive.signin
                    &response_mode=fragment
                    &state={sessionId}
                    &nonce=678910
                    """;

            httpServer.createContext("/minecraft", new MinecraftLinkHandler(this, new MinecraftClientInfo(clientId, callbackURI)));
        }


        httpServer.start();


    }


    private void setupMessages(File messagesFile) {
        saveResource(messagesFile.getName(), false);

        try (FileReader reader = new FileReader(messagesFile)) {
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
        super.onDisable();

        httpServer.stop(0);
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
    public Closeable attachRepeaterTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task,
                                        int delay, int period) {
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
    public Closeable attachAsyncRepeaterTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task,
                                             int delay, int period) {
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
    public Closeable attachAsyncDelayedTask(@NotNull Player player, @NotNull String id, @NotNull Runnable task,
                                            int delay) {
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
    public Map<Player, PlayerDisplay> getDisplayMap() {
        return this.displayMap;
    }

    @Override
    public Cache<String, Execution> getAuthorizationCache() {
        return authorizationCache;
    }
}

