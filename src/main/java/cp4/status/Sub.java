package cp4.status;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import static cp4.status.listeners.prefixPlugin;


public class Sub implements CommandExecutor {

    private String subtitle = "TEST";

    public Sub(Main main) {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int fadeIn = 20;
        int stay = 40;
        int fadeOut = 20;

        Player player = (Player) sender;
        if (player.hasPermission("cp4.sub")) {
            player.sendMessage("Du leuchtest jetzt");
            player.setGlowing(true);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            player.sendMessage(prefixPlugin + "§3Du leuchtest jetzt");



        }
        else player.sendMessage(prefixPlugin + "§3Du musst Sub bei Oasis4_0 oder Oreocast sein");


        return true;


    }

}
