package de.lebaasti.craftprotect4

import de.lebaasti.craftprotect4.functions.luckPerms
import de.lebaasti.craftprotect4.listeners.ChatListener
import de.lebaasti.craftprotect4.listeners.TablistManager
import de.lebaasti.craftprotect4.listeners.luckPermsListener
import org.bukkit.Bukkit

    fun registerEvents() {
        Bukkit.getPluginManager().registerEvents(TablistManager(), instance)
        Bukkit.getPluginManager().registerEvents(ChatListener(), instance)
        luckPermsListener(luckPerms)
    }
