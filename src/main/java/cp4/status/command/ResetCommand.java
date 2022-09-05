package cp4.status.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cp4.status.Listeners.prefixPlugin;

public class ResetCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl ausführen.");
            return true;
        }

        Player player = (Player) sender;
        player.setPlayerListName(player.getDisplayName());
        player.setGlowing(false);
        player.sendMessage(prefixPlugin + "§3Alles wurde von dir zurückgesetzt");

        return true;
    }
}
