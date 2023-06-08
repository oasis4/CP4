package com.github.oasis.craftprotect.api;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Closeable;

public interface Feature<P extends JavaPlugin> extends Listener, Closeable {

    void init(P plugin);

}
