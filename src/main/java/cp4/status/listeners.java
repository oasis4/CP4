package cp4.status;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class listeners implements Listener {

    public static String prefixPlugin = "§b[CP4 Plugin§b] ";

    private Main pl;

    public listeners(Main pl) {
        this.pl = pl;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        Location location = player.getLocation();
        Location locationfire = player.getLocation().clone();
        player.setGlowing(false);
        // p.sendTitle("Willkommen", "", 1, 40, 1);

        e.setJoinMessage("§8[§b+§8] " + player.getDisplayName());

        // Math.cos(0) = 1
        // Math.sin(0) = 0

        // x = 1
        // z = 0

        //Rakete

        File userDataFolder = pl.getUserDataFolder();
        File userData = new File(userDataFolder, player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        long onlineTime = configuration.getLong("online-time", 0L);


        if (onlineTime >= 604800000) {
            player.setPlayerListName("§6GOLD  " + player.getDisplayName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        } else if (onlineTime >= 86400000) {
            player.setPlayerListName("§eAktiv " + player.getDisplayName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        } else if (onlineTime >= 18000000) {
            player.setPlayerListName("§7Neu " + player.getDisplayName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        }


        double seconds = 3.0;
        double segments = 16;
        double radius = 1;

        new BukkitRunnable() {
            final long created = System.currentTimeMillis();


            @Override
            public void run() {
                if ((created + seconds * 1000) < System.currentTimeMillis()) {
                    cancel();
                    return;
                }
                for (double pa = 0.0; pa < 2 * Math.PI; pa += 2 * Math.PI / segments) {
                    Location l = locationfire.clone()
                            .add(
                                    Math.cos(pa) * radius, // X
                                    0.3, // Y
                                    Math.sin(pa) * radius // Z
                            );
                    world.spawnParticle(Particle.FLAME, l, 1, 0, 0, 0, 0);
                    Location l2 = l.clone().add(0, 0.4, 0);
                    world.spawnParticle(Particle.FLAME, l2, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimerAsynchronously(pl, 10, 10);

        // if (p.isOp()) {
        //p.sendMessage("§7Willkommen Admin");


    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (player.hasPermission("cp4.admin")) {
            e.setFormat("§c%s §c» §c%s");
        } else if (player.hasPermission("cp4.mod")) {
            e.setFormat("§2%s §6» §6%s");
        }
         else if (player.hasPermission("cp4.sub")) {
            e.setFormat("§6%s §5» §5%s");
        } else {
            e.setFormat("§2%s §8» §7%s");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§7" + e.getPlayer().getDisplayName() + " hat das Spiel Verlassen!");
    }


}
