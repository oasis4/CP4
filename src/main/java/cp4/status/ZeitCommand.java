package cp4.status;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static cp4.status.listeners.prefixPlugin;


public class ZeitCommand implements CommandExecutor, Listener {

    private Main pl;

    public ZeitCommand(Main pl) {
        this.pl = pl;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setMetadata("last-joined", new FixedMetadataValue(pl, System.currentTimeMillis()));
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDu musst ein Spieler sein.");
            return true;
        }

        Player player = (Player) sender;

        File userDataFolder = pl.getUserDataFolder();
        File userData = new File(userDataFolder, player.getUniqueId() + ".yml");

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        long onlineTime = configuration.getLong("online-time", 0L);


        List<MetadataValue> list = player.getMetadata("last-joined");
        long lastJoined = 0;
        if (!list.isEmpty()) {
            lastJoined = list.get(0).asLong();
        }
        onlineTime += (System.currentTimeMillis() - lastJoined);

        sender.sendMessage(prefixPlugin + "§3Deine Spielzeit beträgt: §6 " + DurationFormatUtils.formatDuration(onlineTime, "DD:HH:mm:ss"));

        return true;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setMetadata("last-joined", new FixedMetadataValue(pl, System.currentTimeMillis()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayer(player);
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (!pl.equals(event.getPlugin()))
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayer(player);
        }
    }

    private void savePlayer(Player player) {
        File userDataFolder = pl.getUserDataFolder();
        File userData = new File(userDataFolder, player.getUniqueId() + ".yml");

        if (!userData.isFile()) {
            try {
                userData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        long onlineTime = configuration.getLong("online-time", 0L);

        List<MetadataValue> list = player.getMetadata("last-joined");
        long lastJoined = 0;
        if (!list.isEmpty()) {
            lastJoined = list.get(0).asLong();
        }

        onlineTime += (System.currentTimeMillis() - lastJoined);

        configuration.set("online-time", onlineTime);
        try {
            configuration.save(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.removeMetadata("last-joined", pl);
    }
}