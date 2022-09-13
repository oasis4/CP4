package de.lebaasti.craftprotect4

import cp4.status.CP4Plugin
import de.lebaasti.craftprotect4.functions.luckPerms
import de.lebaasti.craftprotect4.listeners.ChatListener
import de.lebaasti.craftprotect4.listeners.TablistManager
import de.lebaasti.craftprotect4.listeners.luckPermsListener
import org.bukkit.Bukkit


val instance = CP4Plugin.getPlugin(CP4Plugin::class.java)

    fun registerEvents() {
        Bukkit.getPluginManager().registerEvents(TablistManager(), instance)
        Bukkit.getPluginManager().registerEvents(ChatListener(), instance)
        luckPermsListener(luckPerms)
    }
