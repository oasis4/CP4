package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.controller.PlayerDisplayController;
import com.github.oasis.craftprotect.feature.AfkFeature;
import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class AfkCommand implements CraftProtectCommand {

    @Inject
    private AfkFeature feature;
    @Inject
    private PlayerDisplayController controller;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        Location remove = feature.getLocationMap().remove(player);
        if (remove != null) {
            PlayerDisplayModel model = controller.get(player);
            model.setAwayFromKeyboard(false);
            controller.update(player, model);
            return true;
        }

        Location position = player.getLocation();
        feature.getLocationMap().put(player, position.getBlock().getLocation().clone());

        PlayerDisplayModel model = controller.get(player);
        model.setAwayFromKeyboard(true);
        controller.update(player, model);
        return true;

    }
}
