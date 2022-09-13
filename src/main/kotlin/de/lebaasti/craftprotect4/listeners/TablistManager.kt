package de.lebaasti.craftprotect4.listeners

import de.lebaasti.craftprotect4.functions.*
import org.bukkit.Bukkit
import org.bukkit.Statistic
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerStatisticIncrementEvent
import java.text.DecimalFormat

class TablistManager : Listener {

/*    val timer = Timer().schedule(object : TimerTask() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach { player ->
                    if (player.hasPermission("craftprotect4.rgbname")) {
                        val group = player.group ?: return
                        val chatColor = group.color
                        var newString = ""
                        newString += "$chatColor${group.displayName} "
                        val name = player.name
                        for (i in name.indices) {
                            val char = name[i]
                            val longHue = System.currentTimeMillis() - i * 100
                            val color = Color(Color.HSBtoRGB(longHue % 3000L / 3000.0f, 1.0f, 1.0f)).toChatColor()
                            newString += "$color$char"
                        }
                        newString += " §8(§c${player.deaths} ☠§8)"
                        player.setPlayerListName(newString)
                    }
                }

            }
        }, 1, 100)*/

    fun Int.toBinaryString() : String {
        val decimalFormat = DecimalFormat("00")
        return decimalFormat.format(this)

    }

    private fun Player.setTablist() {
        val scoreboard = Bukkit.getScoreboardManager()?.newScoreboard ?: return
        luckPerms.groupManager.loadedGroups.forEach {
            val name = "${(100 - it.weight.orElse(-1)).toBinaryString()}${it.displayName}"
            if(scoreboard.getTeam(name) == null) {
                scoreboard.registerNewTeam(name).prefix = "${it.displayName} §f"
            }
        }
        for (it in Bukkit.getOnlinePlayers()) {
            val group = it.group ?: continue
            val intString = (100-group.weight.orElse(-1)).toBinaryString()
            val s = "$intString${group.displayName}"
            scoreboard.getTeam(s)?.addEntry(it.name)
        }
        this.updateTablist()
        this.scoreboard = scoreboard
    }

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        event.player.setPlayerListHeaderFooter("§2\n§7\n§e\n\uE217\n§a\n§b\n§c", "")
        Bukkit.getOnlinePlayers().forEach {
            it.setTablist()
        }
    }


    @EventHandler
    fun onPlayerStatisticIncrementEvent(event: PlayerStatisticIncrementEvent) {
        if(event.statistic == Statistic.DEATHS) {
            event.player.updateTablist(event.newValue)
        } else if (event.statistic == Statistic.TOTAL_WORLD_TIME) {
            if(event.newValue / 20 >= 504000) { //7 Stunden

            }
        }
    }


}