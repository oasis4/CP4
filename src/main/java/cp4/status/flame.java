package cp4.status;


import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class flame implements CommandExecutor {

public final Main main;
    ;

    public flame(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        World world = player.getWorld();
        Location location = player.getLocation();

        double seconds = 3.0;
        double segments = 16;


        new BukkitRunnable() {
            final long created = System.currentTimeMillis();


            @Override
            public void run() {
                if ((created + seconds * 1000) < System.currentTimeMillis()) {
                    cancel();
                    return;
                }






                if (args[0] == "on") {
                    for (double pa = 0; pa < 8; pa ++) {
                        location.setX(location.getX()   + 1);

                        Location l = location


                                .add(

                                        2, // X
                                        0, // Y
                                        0 // Z
                                );
                        world.spawnParticle(Particle.FLAME, l, 1, 0, 0, 0, 0);

                    }
                }
            }
        }.runTaskTimerAsynchronously(main, 10, 10);


        return true;
    }




}



