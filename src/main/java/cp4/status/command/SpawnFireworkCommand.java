package cp4.status.command;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import static cp4.status.Listeners.prefixPlugin;

public class SpawnFireworkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl ausführen.");
            return true;
        }
        if (!(sender.hasPermission("cp4.sub"))) {
            sender.sendMessage(prefixPlugin + "§3Du musst Sub bei Oasis4_0 oder Oreocast sein");
            return true;
        }

        Player player = (Player) sender;

        Location location = player.getLocation();

        Firework fireWork = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.PURPLE).build());
        fireworkMeta.setPower(3);

        fireWork.setFireworkMeta(fireworkMeta);
        return true;
    }
}
