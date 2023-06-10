package com.github.oasis.craftprotect;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.storage.AsyncUserStorage;
import com.github.oasis.craftprotect.storage.UserStorage;
import com.google.inject.AbstractModule;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftProtectInjectModule extends AbstractModule {

    private final CraftProtectPlugin plugin;

    public CraftProtectInjectModule(CraftProtectPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(Plugin.class).to(CraftProtectPlugin.class);
        bind(JavaPlugin.class).to(CraftProtectPlugin.class);
        bind(CraftProtect.class).to(CraftProtectPlugin.class);
        bind(CraftProtectPlugin.class).toInstance(plugin);
        bind(UserStorage.class).to(AsyncUserStorage.class);
        bind(AsyncUserStorage.class).toInstance(plugin.getUserStorage());
        bind(HttpServer.class).toInstance(this.plugin.getHttpServer());
    }
}
