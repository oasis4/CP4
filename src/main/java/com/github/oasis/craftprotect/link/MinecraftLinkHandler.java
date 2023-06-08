package com.github.oasis.craftprotect.link;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.feature.LiveStreamFeature;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MinecraftLinkHandler implements HttpHandler {

    private final CraftProtectPlugin plugin;
    private final MinecraftClientInfo info;

    public MinecraftLinkHandler(CraftProtectPlugin plugin, MinecraftClientInfo info) {
        this.plugin = plugin;
        this.info = info;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
