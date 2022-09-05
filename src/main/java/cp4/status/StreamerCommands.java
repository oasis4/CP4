package cp4.status;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StreamerCommands implements CommandExecutor {

    private final CP4Plugin plugin;

    public StreamerCommands(CP4Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDu musst ein Spieler sein.");
            return true;
        }
        if(!sender.hasPermission("cp4.streamer")) {
            sender.sendMessage("§cDir fehlen Berechtigungen um dies zutun.");
            return true;
        }

        Player player = (Player) sender;
        player.setPlayerListName("§1Live " + player.getDisplayName());
        return true;
    }


}