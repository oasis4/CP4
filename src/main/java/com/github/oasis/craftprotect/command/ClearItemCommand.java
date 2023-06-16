package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

@Singleton
public class ClearItemCommand implements CraftProtectCommand {


    @Inject
    private CraftProtect plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player player = (Player) commandSender;

        if (!player.hasPermission("cp4.admin")) {
            plugin.sendMessage(player, M.NO_PLAYER);
            return true;
        }


        int countdownTime = 10;
        new BukkitRunnable() {
            int countdown = countdownTime;

            @Override
            public void run() {
                if (countdown == 0) {
                    clearItems();
                    cancel();
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Alle Item-Drops wurden gelöscht!");
                } else {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Die Item-Drops werden in " + countdown + " Sekunden gelöscht.");
                    countdown--;
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Führe die Aufgabe alle 20 Ticks (1 Sekunde) aus
        return false;
    }

    public long clearItems() {
        return getServer().getWorlds()
                .stream()
                .flatMap(world -> world.getEntitiesByClass(Item.class).stream())
                .mapToLong(value -> {
                    value.remove();
                    return 1;
                }).sum();
    }
}