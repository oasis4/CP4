package com.github.oasis.craftprotect.config;

import com.github.oasis.craftprotect.link.MinecraftClientInfo;
import com.github.oasis.craftprotect.link.TwitchClientInfo;
import lombok.Data;

@Data
public class CraftProtectConfig {

    private SQLDatabaseConfig database;
    private ChatConfig chat;
    private HttpServerConfig httpServer;
    private TwitchClientInfo twitch;
    private MinecraftClientInfo minecraft;


}
