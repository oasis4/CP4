package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.TwitchTokenRefreshScheduler;
import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectUser;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.storage.AsyncUserStorage;
import com.github.oasis.craftprotect.utils.PlayerDisplay;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.domain.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class LiveStreamFeature implements Feature<CraftProtectPlugin> {

    private CraftProtect craftProtect;
    private Map<Player, CraftProtectUser> userMap = new WeakHashMap<>();

    private TwitchTokenRefreshScheduler twitchTokenRefreshScheduler;
    private TwitchClient twitchClient;
    private Map<Player, User> subscribedAccounts = new WeakHashMap<>();

    private final String clientId, clientSecret;

    public LiveStreamFeature(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public void init(CraftProtectPlugin plugin) {
        this.craftProtect = plugin;
        this.twitchTokenRefreshScheduler = new TwitchTokenRefreshScheduler(plugin.getLogger(), clientId, clientSecret);

        twitchClient = TwitchClientBuilder.builder().withClientId(clientId).withClientSecret(clientSecret).withEnableHelix(true).build();

        twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class, event -> {
            subscribedAccounts.entrySet().stream()
                    .filter(playerUserEntry -> playerUserEntry.getValue().getId().equals(event.getChannel().getId()))
                            .forEach(playerUserEntry -> {
                                Player key = playerUserEntry.getKey();
                                PlayerDisplay playerDisplay = craftProtect.getPlayerDisplay(key);
                                playerDisplay.setLive(true);
                            });
            Bukkit.broadcastMessage("LIVE! " + event.getEventId() + " AA");
        });

        twitchClient.getEventManager().onEvent(ChannelGoOfflineEvent.class, event -> {
            subscribedAccounts.entrySet().stream()
                    .filter(playerUserEntry -> playerUserEntry.getValue().getId().equals(event.getChannel().getId()))
                    .forEach(playerUserEntry -> {
                        Player key = playerUserEntry.getKey();
                        PlayerDisplay playerDisplay = craftProtect.getPlayerDisplay(key);
                        playerDisplay.setLive(false);
                    });
            Bukkit.broadcastMessage("Offline! " + event.getEventId() + " AA");
        });

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        AsyncUserStorage userStorage = craftProtect.getUserStorage();
        userStorage.findUserAsync(event.getPlayer().getUniqueId()).thenAccept(userOptional -> {
            System.out.println(userOptional.isPresent());
            CraftProtectUser user;
            if (userOptional.isEmpty()) {
                user = new CraftProtectUser(event.getPlayer().getUniqueId(), null, null);
                userMap.put(event.getPlayer(), user);
                userStorage.persist(user);
            } else {
                user = userOptional.get();
                userMap.put(event.getPlayer(), user);
            }

            if (user.getTwitchId() != null) {
                Bukkit.broadcastMessage("Subscribe and lookup");
                twitchClient.getHelix().getUsers(null, List.of(user.getTwitchId()), null).execute().getUsers().stream().findFirst()
                        .ifPresent(twitchUser -> {
                            twitchClient.getClientHelper().enableStreamEventListener(twitchUser.getId(), twitchUser.getLogin());
                            this.subscribedAccounts.put(event.getPlayer(), twitchUser);
                        });
                boolean isLive = twitchClient.getHelix().getStreams(null, null, null, "live", 1, null, null,  List.of(user.getTwitchId()), null).execute().getStreams().size() > 0;
                PlayerDisplay playerDisplay = craftProtect.getPlayerDisplay(event.getPlayer());
                playerDisplay.setLive(isLive);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        User user1 = this.subscribedAccounts.get(event.getPlayer());
        if(user1 == null)
            return;
        twitchClient.getClientHelper().disableStreamEventListenerForId(user1.getId());
        PlayerDisplay playerDisplay = craftProtect.getPlayerDisplay(event.getPlayer());
        playerDisplay.setLive(false);
    }

    @Override
    public void close() throws IOException {
        if (this.twitchTokenRefreshScheduler != null) this.twitchTokenRefreshScheduler.close();

        if (this.twitchClient != null) {
            for (Map.Entry<Player, User> entry : this.subscribedAccounts.entrySet()) {
                this.twitchClient.getClientHelper().disableStreamEventListenerForId(entry.getValue().getId());
            }
            craftProtect.getDisplayMap().forEach((player, playerDisplay) -> playerDisplay.setLive(false));
            this.twitchClient.close();
        }
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }
}
