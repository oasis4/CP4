package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.api.Feature;
import com.github.oasis.craftprotect.controller.ChallengeController;
import com.github.oasis.craftprotect.model.ChallengeModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;

@Singleton
public class ChallengeFeature implements Feature {

    @Inject
    private ChallengeController controller;


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ChallengeModel model = controller.getModel(player);
        model.setCurrent(model.getCurrent() + 1);
        controller.putModel(player, model);
    }

    @Override
    public void close() throws IOException {

    }
}
