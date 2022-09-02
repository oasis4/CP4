package cp4.status;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;




public class teamchat implements CommandExecutor {


    String prefix = "§b[CP4 Plugin§b] ";


    private final String main;

    public teamchat(String main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player p = (Player) commandSender;



        if (!p.hasPermission("cp4.mod")) {
            p.sendMessage(prefix + "§cDafür hast du keine Rechte!");
            return true;

        }



        return true;
    }
}