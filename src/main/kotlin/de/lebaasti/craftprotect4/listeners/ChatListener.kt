package de.lebaasti.craftprotect4.listeners

import de.lebaasti.craftprotect4.functions.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener : Listener {

    @EventHandler
    fun onAsyncPlayerChatEvent(event: AsyncPlayerChatEvent) {
        val player = event.player
        val group = player.group ?: return
        event.format = "${group.displayName} §f§r${group.color}${player.name} §7» ${player.user.chatColor}${event.message}"
    }

    //@EventHandler
    //public void onAsyncPlayerChatEvent(event: AsyncPlayerChatEvent) {
    //Player player = event.player




}