package com.github.oasis.craftprotect.feature.Combat;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class Combat implements Listener {

    BossBar bossBar;

    private final CraftProtectPlugin plugin;

    public Combat(CraftProtectPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent cause) {


        if (cause.getDamager() instanceof Player) {
            Bossbar();
            Player player = (Player) cause.getDamager();
            bossBar.addPlayer(player);
            plugin.getLogger().info("Combat is working");

        }
        else plugin.getLogger().info("Combat is not working");

    }


    private void Bossbar() {
        //
        //BossBar bar = Bukkit.getServer().createBossBar("Combat", org.bukkit.boss.BarColor.RED, org.bukkit.boss.BarStyle.SOLID);
      //  bar.setVisible(true);

        bossBar = Bukkit.createBossBar(
                ChatColor.DARK_PURPLE + "Combat " + ChatColor.LIGHT_PURPLE + "...",
                BarColor.PURPLE,
                BarStyle.SOLID);
    }


    @EventHandler

    public void PlayerDeathEvent(Player player) {
            if (player.getHealth() == 0) {
                bossBar.removePlayer(player);
                plugin.getLogger().info("Death is working");
                plugin.getLogger().info(player.getName());
            }
        }
    }



