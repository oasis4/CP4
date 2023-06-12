package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.TwitchTokenRefreshScheduler;
import com.github.oasis.craftprotect.api.CraftProtectUser;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.PlayerDisplayController;
import com.github.oasis.craftprotect.link.TwitchClientInfo;
import com.github.oasis.craftprotect.link.TwitchLinkHandler;
import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import com.github.oasis.craftprotect.storage.AsyncUserStorage;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.domain.User;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Singleton
public class LiveStreamFeature implements Feature {

    @Inject
    private PlayerDisplayController controller;

    @Inject
    private AsyncUserStorage userStorage;

    private final TwitchTokenRefreshScheduler twitchTokenRefreshScheduler;
    private final TwitchClient twitchClient;
    private final Map<Player, User> subscribedAccounts = new WeakHashMap<>();

    @Inject
    public LiveStreamFeature(CraftProtectPlugin craftProtect, HttpServer server) {

        TwitchClientInfo twitch = craftProtect.getCraftProtectConfig().getTwitch();
        String clientId = twitch.getClientId();
        String clientSecret = twitch.getClientSecret();

        this.twitchTokenRefreshScheduler = new TwitchTokenRefreshScheduler(craftProtect.getLogger(), clientId, clientSecret);

        twitchClient = TwitchClientBuilder.builder().withClientId(clientId).withClientSecret(clientSecret).withEnableHelix(true).build();

        twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class, event -> subscribedAccounts.entrySet().stream()
                .filter(playerUserEntry -> playerUserEntry.getValue().getId().equals(event.getChannel().getId()))
                .forEach(playerUserEntry -> {
                    Player key = playerUserEntry.getKey();
                    controller.update(key, model -> model.setLive(true));

                    PlayerDisplayModel playerDisplay = controller.get(key);
                    playerDisplay.setLive(true);
                    controller.update(key, playerDisplay);
                }));

        twitchClient.getEventManager().onEvent(ChannelGoOfflineEvent.class, event -> subscribedAccounts.entrySet().stream()
                .filter(playerUserEntry -> playerUserEntry.getValue().getId().equals(event.getChannel().getId()))
                .forEach(playerUserEntry -> {
                    Player key = playerUserEntry.getKey();
                    controller.update(key, model -> model.setLive(false));
                }));

        server.createContext("/twitch", new TwitchLinkHandler(craftProtect, twitch, this));
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        userStorage.findUserAsync(event.getPlayer().getUniqueId()).thenAccept(userOptional -> {
            CraftProtectUser user;
            if (userOptional.isEmpty()) {
                user = new CraftProtectUser(event.getPlayer().getUniqueId(), null, null);
                userStorage.persist(user);
            } else {
                user = userOptional.get();
            }

            if (user.getTwitchId() != null) {
                twitchClient.getHelix().getUsers(null, List.of(user.getTwitchId()), null).execute().getUsers().stream().findFirst()
                        .ifPresent(twitchUser -> {
                            twitchClient.getClientHelper().enableStreamEventListener(twitchUser.getId(), twitchUser.getLogin());
                            this.subscribedAccounts.put(event.getPlayer(), twitchUser);
                        });
                boolean isLive = twitchClient.getHelix().getStreams(null, null, null, "live", 1, null, null, List.of(user.getTwitchId()), null).execute().getStreams().size() > 0;
                controller.update(event.getPlayer(), model -> model.setLive(isLive));
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        User user1 = this.subscribedAccounts.get(event.getPlayer());
        if (user1 == null)
            return;
        twitchClient.getClientHelper().disableStreamEventListenerForId(user1.getId());
        controller.update(event.getPlayer(), model -> model.setLive(false));
    }

    @Override
    public void close() throws IOException {
        if (this.twitchTokenRefreshScheduler != null) this.twitchTokenRefreshScheduler.close();

        if (this.twitchClient != null) {
            for (Map.Entry<Player, User> entry : this.subscribedAccounts.entrySet()) {
                this.twitchClient.getClientHelper().disableStreamEventListenerForId(entry.getValue().getId());
            }
            controller.updateAll((player, model) -> model.setLive(false));
            this.twitchClient.close();
        }
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }
}
