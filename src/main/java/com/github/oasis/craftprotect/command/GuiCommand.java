package com.github.oasis.craftprotect.command;


import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.feature.GuiFeature;
import com.github.oasis.craftprotect.utils.M;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;



public class GuiCommand implements CraftProtectCommand {




    public final CraftProtect plugin;

    public GuiCommand(CraftProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        GuiFeature guiFeature = new GuiFeature(player);

        guiFeature.openInventory(player);




        return true;
    }
}













