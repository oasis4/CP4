package cp4.status;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class rw implements CommandExecutor {

    public rw(Main main) {
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        Location location = player.getLocation();

        Firework fireWork = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.PURPLE).build());
        fireworkMeta.setPower(1);
        fireWork.setFireworkMeta(fireworkMeta);


        return true;
    }
}
