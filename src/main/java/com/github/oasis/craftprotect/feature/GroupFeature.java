package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.CraftProtect;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GroupFeature implements Listener {

    private final CraftProtect plugin;

    public GroupFeature(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Math.cos(0) = 1
        // Math.sin(0) = 0

        // x = 1
        // z = 0

        //Rakete
        long onlineTime = plugin.getUptime(player);

        if (onlineTime >= 604800000) {
            player.setPlayerListName("§6GOLD§7・" + player.getDisplayName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        } else if (onlineTime >= 86400000) {
            player.setPlayerListName("§eAktiv§7・" + player.getDisplayName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        } else if (onlineTime >= 18000000) {
            player.setPlayerListName("§7Neu§7・" + player.getDisplayName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        }

    }
}
