package com.github.oasis.craftprotect.utils;

import org.bukkit.Location;

public final class MoveUtils {

    public static boolean movedBlock(Location from, Location to) {
        return from.getWorld() != to.getWorld() || from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
    }


}
