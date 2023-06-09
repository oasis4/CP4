package com.github.oasis.craftprotect.view;

import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import org.bukkit.entity.Player;

public final class TabListView {

    public static void update(Player player, PlayerDisplayModel model) {
        player.setPlayerListName(model.toTabList(player.getName()));
    }

}
