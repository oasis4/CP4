package cp4.status;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class listeners implements Listener {

    public static String prefixPlugin = "§b[CP4 Plugin§b] ";

    private Main pl;

    public listeners(Main pl) {
        this.pl = pl;
    }


    @EventHandler
    public void onLogin(PlayerJoinEvent e) {

        Player p = e.getPlayer();


         // if (p.isOp()) {
             //p.sendMessage("§7Willkommen Admin");

            e.setJoinMessage(prefixPlugin + p.getDisplayName() + " §e hat das Spiel betreten");

    }


   @EventHandler
    public void onQuit(PlayerQuitEvent e){

        e.setQuitMessage( prefixPlugin + e.getPlayer().getDisplayName() + " §ehat das Spiel verlassen" );
    }




}
