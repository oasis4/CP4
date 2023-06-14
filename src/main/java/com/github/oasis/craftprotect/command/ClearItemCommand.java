package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

public class ClearItemCommand implements CraftProtectCommand {

    @Inject
    private CraftProtect plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

       Player player = (Player) commandSender;

        if (!player.hasPermission("cp4.admin")) {
            plugin.sendMessage(player, M.NO_PLAYER);
            return true;
        }

                int amount=0;
                for(int i=0;i<getServer().getWorlds().size();i++) {
                    for(int j=0;j<getServer().getWorlds().get(i).getEntities().size();j++) {
                        if(getServer().getWorlds().get(i).getEntities().get(j).getType().equals(EntityType.DROPPED_ITEM)) {
                            getServer().getWorlds().get(i).getEntities().get(j).remove();
                            amount++;
                        }
                    }
                }
               //for(int i=0; i<getServer().getOnlinePlayers().length ;i++) {
                //    getServer().getOnlinePlayers()[i].sendMessage("[Cp5]Clear "+amount+" Drop Item!");
           //   }

        return true;
    }
}
