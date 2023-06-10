package com.github.oasis.craftprotect.controller;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Singleton
public class SpawnController {

    private final File file;
    private Location location;

    @Inject
    public SpawnController(CraftProtectPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "spawn.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        this.location = (Location) yamlConfiguration.get("spawn");
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) throws IOException {
        this.location = location;
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("spawn", location);
        configuration.save(file);
    }
}
