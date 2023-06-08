package com.github.oasis.craftprotect.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FeaturedPlugin extends JavaPlugin {

    private final Set<Feature<?>> features = new HashSet<>();

    public final <T extends JavaPlugin> void loadFeature(Feature<T> feature) {
        feature.init((T) this);
        getServer().getPluginManager().registerEvents(feature, this);
    }

    @Override
    public void onDisable() {
        for (Feature<?> feature : features) {
            try {
                feature.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<Feature<?>> getFeatures() {
        return features;
    }
}
