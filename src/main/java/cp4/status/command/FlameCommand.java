package cp4.status.command;


import cp4.status.CP4Plugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FlameCommand implements CommandExecutor {


    public final CP4Plugin plugin;

    public FlameCommand(CP4Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        World world = player.getWorld();
        Location location = player.getLocation();

        world.spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0);
        return true;
    }

}













