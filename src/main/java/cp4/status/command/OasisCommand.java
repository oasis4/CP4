package cp4.status.command;

import cp4.status.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cp4.status.listeners.prefixPlugin;

public class OasisCommand implements CommandExecutor {
    public OasisCommand(Main main) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (!sender.hasPermission("cp4.admin")) {
            sender.sendMessage(prefixPlugin + "§cDas kann nur Oasis");
            return true;
        }

        TextComponent c = new TextComponent(prefixPlugin + "§3Oasis ist nun Live auf Twitch schaut rein: ");

        TextComponent clickme = new TextComponent("§5Twitch");
        clickme.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.twitch.tv/oasis4_0"));
        clickme.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3Oasis Twitch!")));

        c.addExtra(clickme);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(c);
        }
        return true;
    }

}
