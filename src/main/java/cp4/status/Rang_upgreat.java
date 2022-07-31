package cp4.status;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.server.BroadcastMessageEvent;





public class Rang_upgreat implements CommandExecutor {

    public Rang_upgreat(Main main) {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
       Sound AMBIENT_BASALT_DELTAS_ADDITIONS;
       player.sendMessage("TEST");

        return true;
    }

}
