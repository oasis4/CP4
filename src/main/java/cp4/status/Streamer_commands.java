package cp4.status;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Streamer_commands implements CommandExecutor {

    private final Main main;

    public Streamer_commands(Main main) {
        this.main = main;
    }


    Scoreboard sb;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        if (commandSender instanceof Player) {
            Player spieler = (Player) commandSender;

            if (spieler.hasPermission("cp4.streamer")) {

                spieler.setPlayerListName("§1Live " + spieler.getDisplayName());

                return true;
            }
        }

        return false;
    }


}