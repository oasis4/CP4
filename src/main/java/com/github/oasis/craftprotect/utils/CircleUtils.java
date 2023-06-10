package com.github.oasis.craftprotect.utils;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class CircleUtils {

    public static final int CIRCLE_SEGMENTS = 16;
    public static final int CIRCLE_RADIUS = 1;

    private static final List<Vector> circleLocations = new ArrayList<>(CIRCLE_SEGMENTS);

    static {
        for (double pa = 0.0; pa < 2 * Math.PI; pa += 2 * Math.PI / CIRCLE_SEGMENTS) {
            circleLocations.add(new Vector(Math.cos(pa) * CIRCLE_RADIUS, 0, Math.sin(pa) * CIRCLE_RADIUS));
        }
    }

    public static Collection<Vector> getCircleLocations() {
        return Collections.unmodifiableCollection(circleLocations);
    }
}
