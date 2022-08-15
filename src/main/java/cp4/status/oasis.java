package cp4.status;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cp4.status.listeners.prefixPlugin;

public class oasis implements CommandExecutor {
    public oasis(Main main) {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player) commandSender;

        if (player.hasPermission("cp4.admin")) {

            TextComponent c = new TextComponent("§3Ich bin Live: ");
            TextComponent clickme = new TextComponent("clickme");

            clickme.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.twitch.tv/oasis4_0"));

            clickme.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT , TextComponent.fromLegacyText("§3Oasis Twitch!")));

            c.addExtra(clickme);
            // player.spigot().sendMessage(c);


            return true;
        }
        else player.sendMessage(prefixPlugin + "§3Das kann nur Oasis");
        return true;
    }

}
