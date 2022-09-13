package de.lebaasti.craftprotect4.functions

import org.bukkit.Statistic
import org.bukkit.entity.Player

val Player.deaths get() = getStatistic(Statistic.DEATHS)

fun Player.updateTablist(deaths: Int = this.getStatistic(Statistic.DEATHS)) {
    val group = group ?: return
    this.setPlayerListName("${group.displayName} §7${this.name} §8(§c$deaths ☠§8)")
}
