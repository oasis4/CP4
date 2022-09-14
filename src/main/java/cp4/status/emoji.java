package cp4.status;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class emoji implements Listener {

    public emoji(CP4Plugin cp4Plugin) {
    }

   String test = "Moin" ;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        String msg = e.getMessage();
        if(msg.contains(test)){
            e.setMessage("123");

        }
    }




}
