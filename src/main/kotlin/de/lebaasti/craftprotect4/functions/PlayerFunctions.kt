package de.lebaasti.craftprotect4.functions

import org.bukkit.Statistic
import org.bukkit.entity.Player
import java.util.StringJoiner

val playerStatus = mutableMapOf<Player, Status>()

var Player.status: Status
    get() {
        return playerStatus[this] ?: Status.NONE
    }
    set(value) {
        playerStatus[this] = value
        this.updateTablist()
    }
val Player.deaths get() = getStatistic(Statistic.DEATHS)

fun Player.updateTablist(deaths: Int = this.getStatistic(Statistic.DEATHS)) {
    val group = group ?: return
    this.setPlayerListName("${status.displayString} ${group.prefix}${group.displayName} §8| §7${this.name} §8(§c$deaths ☠§8)")
}

