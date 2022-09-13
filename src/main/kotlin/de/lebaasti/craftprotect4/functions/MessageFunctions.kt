package de.lebaasti.craftprotect4.functions

import org.bukkit.ChatColor
import java.awt.Color

fun String.translateChatColors(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

fun Color.toChatColor(): net.md_5.bungee.api.ChatColor = net.md_5.bungee.api.ChatColor.of(this)