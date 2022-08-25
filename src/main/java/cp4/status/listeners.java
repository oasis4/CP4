package cp4.status;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

import static org.bukkit.Bukkit.*;

public class listeners implements Listener {

    public static String prefixPlugin = "§b[CP4 Plugin§b] ";

    private Main pl;

    public listeners(Main pl) {
        this.pl = pl;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {







        Player p = e.getPlayer();
        World world = p.getWorld();
        Location location = p.getLocation();
        Location locationfire = p.getLocation().clone();
        p.setGlowing(false);

        e.setJoinMessage(prefixPlugin + p.getDisplayName() + " +");

        // Math.cos(0) = 1
        // Math.sin(0) = 0

        // x = 1
        // z = 0

        //Rakete
        if (p.hasPermission("cp4.sub")) {
            Firework fireWork = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withTrail().withColor(Color.PURPLE).build());
            fireworkMeta.setPower(1);
            fireWork.setFireworkMeta(fireworkMeta);
        }
        if (p.hasPermission("cp4.admin")) {
            Firework fireWork = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withTrail().withColor(Color.RED).build());
            fireworkMeta.setPower(1);
            fireWork.setFireworkMeta(fireworkMeta);
        }
        if (p.hasPermission("cp4.mod")) {
            Firework fireWork = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withTrail().withColor(Color.ORANGE).build());
            fireworkMeta.setPower(1);
            fireWork.setFireworkMeta(fireworkMeta);
        }
        if (p.hasPermission("cp4.streamer")) {
            Firework fireWork = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withTrail().withColor(Color.BLUE).build());
            fireworkMeta.setPower(1);
            fireWork.setFireworkMeta(fireworkMeta);
        }

        Firework fireWork = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withTrail().withColor(Color.GRAY).build());
        fireworkMeta.setPower(1);
        fireWork.setFireworkMeta(fireworkMeta);


        File userDataFolder = pl.getUserDataFolder();
        File userData = new File(userDataFolder, p.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(userData);
        long onlineTime = configuration.getLong("online-time", 0L);




        if(onlineTime >= 604800000){

            p.setPlayerListName("§6Sehr Aktiv  " + p.getDisplayName());
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        }
        else if (onlineTime >= 86400000){

            p.setPlayerListName("§eAktiv " + p.getDisplayName());
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);


        }
        else if(onlineTime >= 18000000){

            p.setPlayerListName("§7Neu " + p.getDisplayName());
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);


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

                    Location l2 = l.clone().add(0, 0.6, 0);

                    world.spawnParticle(Particle.FLAME, l2, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimerAsynchronously(pl, 10, 10);

        // if (p.isOp()) {
        //p.sendMessage("§7Willkommen Admin");



    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        e.setQuitMessage(e.getPlayer().getDisplayName() + " -");
    }


}
