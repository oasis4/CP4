package com.github.oasis.craftprotect.controller;

import com.github.oasis.craftprotect.api.GroupType;
import com.github.oasis.craftprotect.model.PlayerDisplayModel;
import com.google.inject.Singleton;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;
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
    protected void update0(Player key, PlayerDisplayModel value) {
        this.displayMap.put(key, value);
    }

    public void updateGroup(Player player, long playtime) {
        update(player, display -> {
            if (playtime >= 604800000) {
                display.setGroupType(GroupType.GOLD);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            } else if (playtime >= 86400000) {
                display.setGroupType(GroupType.ACTIVE);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            } else if (playtime >= 18000000) {
                display.setGroupType(GroupType.NEWBIE);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            } else {
                display.setGroupType(GroupType.NEW);
            }
        });
    }

    @Override
    public String toString() {
        return "PlayerDisplayController{" +
                "displayMap=" + displayMap +
                "} " + super.toString();
    }
}
