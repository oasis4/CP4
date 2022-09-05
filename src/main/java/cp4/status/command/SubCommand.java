package cp4.status.command;

import cp4.status.CP4Plugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cp4.status.Listeners.prefixPlugin;


public class SubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender.hasPermission("cp4.sub"))) {
            sender.sendMessage(prefixPlugin + "§3Du musst Sub bei Oasis4_0 oder Oreocast sein");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDu musst ein Spieler sein.");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        player.setGlowing(true);
        player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
        player.sendMessage(prefixPlugin + "§3Du leuchtest jetzt");

        return true;
    }
}






