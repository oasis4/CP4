package cp4.status;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;

public class AFk_Rang implements CommandExecutor, Listener {

    private final Main main;

    public AFk_Rang(Main main) {
        this.main = main;
    }

    Scoreboard sb;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player spieler = (Player) sender;
            Location position = spieler.getLocation();
            World welt = spieler.getWorld();
            double yStart = position.getY();
            double xStart = position.getX();
            double zStart = position.getZ();

            spieler.setPlayerListName("§cAFK " + spieler.getDisplayName());

        }
        return true;

    }

     //@EventHandler
    //public void onMove(PlayerMoveEvent event) {
        //Player player = event.getPlayer();

        //Location to = event.getTo();
       // Location from = event.getFrom();

     //   if (to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockY() || to.getBlockZ() != from.getBlockZ()) {
        //    if (player.getPlayerListName().equalsIgnoreCase(player.getDisplayName()))
       //         return;
      //      player.setPlayerListName(player.getDisplayName());
    //    }

    //}


}









