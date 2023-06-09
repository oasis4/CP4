package com.github.oasis.craftprotect;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.google.inject.AbstractModule;

public class CraftProtectInjectModule extends AbstractModule {

    private final CraftProtectPlugin plugin;

    public CraftProtectInjectModule(CraftProtectPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(CraftProtect.class).to(CraftProtectPlugin.class);
        bind(CraftProtectPlugin.class).toInstance(plugin);
        System.out.println(currentStage());
    }
}
