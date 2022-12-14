package cp4.status.feature;


import cp4.status.CP4Plugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class AFKRankFeature implements CommandExecutor, Listener {

    private final CP4Plugin plugin;

    public AFKRankFeature(CP4Plugin plugin) {
        this.plugin = plugin;
    }

    private final Map<Player, Location> locationMap = new WeakHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

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


}









