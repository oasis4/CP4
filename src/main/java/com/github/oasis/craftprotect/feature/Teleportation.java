package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Teleportation implements Listener {

    private final CraftProtect protect;

    public Teleportation(CraftProtectPlugin plugin) {
        this.protect = plugin;
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()== Material.STRUCTURE_BLOCK){
            player.teleport(new Location(player.getWorld(),189, 100, -125 ));
        }


    }
}