package cp4.status;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.server.BroadcastMessageEvent;





public class Sub implements CommandExecutor {

    public Sub(Main main) {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
       player.sendMessage("Du leuchtest jetzt");
       player.setGlowing(true);

       player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);


        return true;





    }

}
