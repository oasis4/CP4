package com.github.oasis.craftprotect.controller;

import com.github.oasis.craftprotect.model.ChallengeModel;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class ChallengeController {

    private final Map<Player, ChallengeModel> map = new HashMap<>();

    public ChallengeModel getModel(Player player) {
        return map.computeIfAbsent(player, player1 -> new ChallengeModel(
                "Break 100 Blocks",
                "The goal of this challenge is to break 100 blocks of any type to complete this challenge",
                100,
                0
        ));
    }

    public void putModel(Player player, ChallengeModel model) {
        this.map.put(player, model);
    }


}
