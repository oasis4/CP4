package cp4.status;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class reset implements CommandExecutor {

    private Main main;

    public reset(Main main) {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player) commandSender;
        player.setPlayerListName(player.getDisplayName());
        player.setGlowing(false);

        return false;
    }
}
