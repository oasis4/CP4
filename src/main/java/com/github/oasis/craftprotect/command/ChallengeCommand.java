package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.controller.ChallengeController;
import com.github.oasis.craftprotect.utils.M;
import com.github.oasis.craftprotect.view.ChatChallengeView;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ChallengeCommand implements CraftProtectCommand {

    @Inject
    private CraftProtectPlugin plugin;

    @Inject
    private ChallengeController controller;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            this.plugin.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        plugin.getAudiences().sender(player).sendMessage(ChatChallengeView.getDisplay(plugin, controller.getModel(player), 35, 5));

        return true;
    }

}
