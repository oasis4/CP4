package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.api.GroupType;
import com.github.oasis.craftprotect.controller.PlayerDisplayController;
import com.github.oasis.craftprotect.controller.PlaytimeController;
import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

@Singleton
public class GroupFeature implements Feature {

    @Inject
    private PlayerDisplayController controller;

    @Inject
    private PlaytimeController playtimeController;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        PlayerDisplayModel display = controller.get(player);

        playtimeController.getPlaytime(player).thenAccept(onlineTime -> {

            if (onlineTime >= 604800000) {
                display.setGroupType(GroupType.GOLD);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            } else if (onlineTime >= 86400000) {
                display.setGroupType(GroupType.ACTIVE);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            } else if (onlineTime >= 18000000) {
                display.setGroupType(GroupType.NEWBIE);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            } else {
                display.setGroupType(GroupType.NEW);
            }

            controller.update(player, display);
        });
    }

    @Override
    public void close() throws IOException {

    }
}
