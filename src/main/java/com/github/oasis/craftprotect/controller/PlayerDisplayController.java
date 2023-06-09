package com.github.oasis.craftprotect.controller;

import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Singleton
public class PlayerDisplayController extends Controller<Player, PlayerDisplayModel> {

    private final Map<Player, PlayerDisplayModel> displayMap = new WeakHashMap<>();


    public Map<Player, PlayerDisplayModel> getDisplayMap() {
        return displayMap;
    }

    @Override
    public PlayerDisplayModel get(Player key) {
        return getDisplayMap().computeIfAbsent(key, player -> new PlayerDisplayModel());
    }

    @Override
    public Stream<Player> getKeys() {
        return displayMap.keySet().stream();
    }

    @Override
    public Stream<PlayerDisplayModel> getValues() {
        return displayMap.values().stream();
    }

    @Override
    public Stream<Map.Entry<Player, PlayerDisplayModel>> getEntries() {
        return displayMap.entrySet().stream();
    }

    @Override
    public void update(Player key, Consumer<PlayerDisplayModel> modifier) {
        displayMap.compute(key, (player, model) -> {
            if (model == null)
                model = new PlayerDisplayModel();
            modifier.accept(model);
            return model;
        });
    }

    @Override
    protected void update0(Player key, PlayerDisplayModel value) {
        this.displayMap.put(key, value);
    }


    @Override
    public String toString() {
        return "PlayerDisplayController{" +
                "displayMap=" + displayMap +
                "} " + super.toString();
    }
}
