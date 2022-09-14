package de.lebaasti.craftprotect4.functions

import net.md_5.bungee.api.ChatColor

enum class Status(val displayString: String) {

    NONE(""),
    AFK("${ChatColor.of("#446BEB")}AFK"),
    LIVE("")
}