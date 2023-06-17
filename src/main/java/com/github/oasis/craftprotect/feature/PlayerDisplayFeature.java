package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.PlayerDisplayController;
import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import com.github.oasis.craftprotect.view.TabListView;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

@Singleton
public class PlayerDisplayFeature implements Feature {

    private final PlayerDisplayController controller;

    @Inject
    public PlayerDisplayFeature(PlayerDisplayController controller) {
        this.controller = controller;
        this.controller.subscribe(TabListView::update);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDisplayModel playerDisplay = this.controller.get(player);
        TabListView.update(player, playerDisplay);
    }

    @Override
    public void close() throws IOException {

    }
}
