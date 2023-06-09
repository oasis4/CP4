package com.github.oasis.craftprotect.feature;


import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.PlayerDisplayController;
import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

@Singleton
public class AfkFeature implements Feature {

    private final Map<Player, Location> locationMap = new WeakHashMap<>();

    @Inject
    private PlayerDisplayController controller;


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


        PlayerDisplayModel model = controller.get(player);
        model.setAwayFromKeyboard(false);
        controller.update(player, model);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        locationMap.remove(player);

        PlayerDisplayModel model = controller.get(player);
        model.setAwayFromKeyboard(false);
        controller.update(player, model);
    }

    @Override
    public void close() throws IOException {

    }

    public Map<Player, Location> getLocationMap() {
        return locationMap;
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











