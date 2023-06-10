package com.github.oasis.craftprotect.controller;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

@Singleton
public class MotdController {

    private final File file;
    private Component messageOfTheDay;

    @Inject
    public MotdController(CraftProtectPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "motd.txt");
        reloadMotd();
    }

    private void reloadMotd() {
        if (file.isFile()) {
            try (FileReader reader = new FileReader(file)) {
                StringWriter writer = new StringWriter();
                reader.transferTo(writer);
                this.messageOfTheDay = MiniMessage.miniMessage().deserialize(writer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Component getMessageOfTheDay() {
        return messageOfTheDay;
    }
}
