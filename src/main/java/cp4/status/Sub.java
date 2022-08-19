package cp4.status;

import com.hakan.core.HCore;
import com.hakan.core.message.title.HTitle;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;


import static cp4.status.listeners.prefixPlugin;


public class Sub implements CommandExecutor {

    private String subtitle = "TEST";

    public Sub(Main main) {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        Location location = player.getLocation();

        if (player.hasPermission("cp4.sub")) {
            player.sendMessage("Du leuchtest jetzt");
            player.setGlowing(true);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            player.sendMessage(prefixPlugin + "§3Du leuchtest jetzt");

            Firework fireWork = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withTrail().withColor(Color.AQUA).build());
            fireworkMeta.setPower(1);
            fireWork.setFireworkMeta(fireworkMeta);

            HTitle myTitle = new HTitle("Title", "Subtitle", 5, 3, 2); // created a title with title "Title" and subtitle "Subtitle" which appears in 3 ticks, disappears in 2 ticks so totally exists for 5 ticks
            HCore.sendTitle(player, myTitle); // sent title to player


            }
        else player.sendMessage(prefixPlugin + "§3Du musst Sub bei Oasis4_0 oder Oreocast sein");
                return true;

                }
            }






