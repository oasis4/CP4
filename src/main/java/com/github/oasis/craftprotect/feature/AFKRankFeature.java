package com.github.oasis.craftprotect.feature;


import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.api.Feature;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

public class AFKRankFeature implements CraftProtectCommand, Feature<CraftProtectPlugin> {

    private final Map<Player, Location> locationMap = new WeakHashMap<>();

    @Override
    public void init(CraftProtectPlugin plugin) {

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        Location remove = locationMap.remove(player);
        if (remove != null) {
            player.setPlayerListName(player.getDisplayName());
            return true;
        }

        Location position = player.getLocation();
        locationMap.put(player, position.getBlock().getLocation().clone());
        player.setPlayerListName("§cAFK " + player.getDisplayName());
        return true;

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo() == null)
            return;

        Location to = event.getTo().getBlock().getLocation();
        Location from = event.getFrom().getBlock().getLocation();

        if (from.getBlock().getLocation().equals(to.getBlock().getLocation()))
            return;

        Location location = locationMap.get(player);
        if (location == null)
            return;

        if (location.distanceSquared(to) < 25)
            return;

        locationMap.remove(player);
        player.setPlayerListName(player.getDisplayName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        locationMap.remove(event.getPlayer());
    }

    @Override
    public void close() throws IOException {

    }

    //@EventHandler
    //**public void onEntityDamageProtection(EntityDamageByEntityEvent e) {

   //    if (e.getDamager() instanceof Arrow) {
   //         Player p = (Player) e.getDamager();
    //        if (p.hasPermission("oasis.protection")) {
     //           e.setCancelled(true);
    //        }
    //    }
  //  }
}











