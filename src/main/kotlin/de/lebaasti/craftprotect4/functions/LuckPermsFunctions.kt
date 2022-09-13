package de.lebaasti.craftprotect4.functions

import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import net.luckperms.api.node.NodeType
import net.luckperms.api.node.types.MetaNode
import net.md_5.bungee.api.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

val luckPerms get() = LuckPermsProvider.get()

val Group.prefix: String
    get() = cachedData.metaData.prefix ?: ""

val Group.suffix: String?
    get() = cachedData.metaData.suffix

val OfflinePlayer.coloredName: String
    get() {
        return color + this.name
    }

val OfflinePlayer.color: String
    get() {
        return prefix.translateChatColors()
    }

var User.group: Group?
    get() = luckPerms.groupManager.getGroup(primaryGroup)
    set(value) {
        this.primaryGroup = (value?.name ?: return)
        luckPerms.userManager.saveUser(this)
    }

val Group.color: String get() {
    return if(prefix != "") "${ChatColor.of(prefix) ?: ""}" else ""
}

val Player.user: User
    get() {
        return luckPerms.getPlayerAdapter(Player::class.java).getUser(this)
    }

val Player.group: Group?
    get() {
        return luckPerms.groupManager.getGroup(uniqueId.user.primaryGroup)
    }

val OfflinePlayer.prefix: String
    get() {
        val prefix = uniqueId.user.cachedData.metaData.prefix
        return prefix ?: "&a"
    }

var User.chatColor: String
    get() {
        return ChatColor.of(this.cachedData.metaData.getMetaValue("chatcolor") ?: return "§f").toString()
    }
    set(value) {
        val chatColorNode = MetaNode.builder("chatcolor", value).build()
        this.data().clear(NodeType.META.predicate { mn: MetaNode -> mn.metaKey == "chatcolor" })
        this.data().add(chatColorNode)
        luckPerms.userManager.saveUser(this)
    }

var User.tag: String
    get() {
        return ChatColor.of(this.cachedData.metaData.getMetaValue("tag") ?: return "").toString()
    }
    set(value) {
        val chatColorNode = MetaNode.builder("tag", value).build()
        this.data().clear(NodeType.META.predicate { mn: MetaNode -> mn.metaKey == "tag" })
        this.data().add(chatColorNode)
        luckPerms.userManager.saveUser(this)
    }

val UUID.user: User
    get() {
        return luckPerms.userManager.getUser(this) ?: luckPerms.userManager.loadUser(this).get()
    }