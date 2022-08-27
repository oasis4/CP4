package cp4.status;


import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
        UUID uuid = player.getUniqueId();


                        Location l = location;
                        world.spawnParticle(Particle.FLAME, l, 1, 0, 0, 0, 0);

        return false;
    }

                }













