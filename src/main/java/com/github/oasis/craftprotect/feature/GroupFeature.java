package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.api.GroupType;
import com.github.oasis.craftprotect.utils.PlayerDisplay;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class GroupFeature implements Feature<CraftProtectPlugin> {

    private CraftProtect plugin;

    @Override
    public void init(CraftProtectPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Math.cos(0) = 1
        // Math.sin(0) = 0

        // x = 1
        // z = 0


        PlayerDisplay display = plugin.getPlayerDisplay(player);
        display.executeAndSubscribe(playerDisplay -> {
            player.setPlayerListName(playerDisplay.toTabList(player.getDisplayName()));
        });

        long onlineTime = plugin.getUptime(player);
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

    }

    @Override
    public void close() throws IOException {

    }
}
