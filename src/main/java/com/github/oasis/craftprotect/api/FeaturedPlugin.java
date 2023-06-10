package com.github.oasis.craftprotect.api;

import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class FeaturedPlugin extends JavaPlugin {

    private final Set<Feature> features = new HashSet<>();

    protected Injector injector;


    public final <T extends JavaPlugin> void loadFeature(Class<? extends Feature> featureClass) {
        try {
            Feature feature = injector.getInstance(featureClass);
            getServer().getPluginManager().registerEvents(feature, this);
            this.features.add(feature);
        } catch (Exception e) {
            System.err.println("Failed to activate feature: " + featureClass.getCanonicalName());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        for (Feature feature : features) {
            try {
                feature.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public Injector getInjector() {
        return injector;
    }
}
