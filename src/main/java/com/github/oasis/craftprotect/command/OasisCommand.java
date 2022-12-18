package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OasisCommand implements CommandExecutor {

    private final CraftProtect plugin;

    public OasisCommand(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("cp4.admin")) {
            sender.sendMessage(plugin.getPrefix() + "§cDas kann nur Oasis");
            return true;
        }

        TextComponent c = new TextComponent(plugin.getPrefix() + "§3Oasis ist nun Live auf Twitch schaut rein: ");

        TextComponent clickme = new TextComponent("§5Twitch");
        clickme.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.twitch.tv/oasis4_0"));
        //noinspection deprecation
        clickme.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3Oasis Twitch!")));

        c.addExtra(clickme);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(c);
        }
        return true;
    }

}
