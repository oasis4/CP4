package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

public class SpawnFireworkCommand implements CommandExecutor {

    private final CraftProtect protect;

    public SpawnFireworkCommand(CraftProtect protect) {
        this.protect = protect;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(protect.getPrefix() + "§cNur Spieler können diesen Befehl ausführen.");
            return true;
        }
        if (!(sender.hasPermission("cp4.sub"))) {
            sender.sendMessage(protect.getPrefix() + "§3Du musst Sub bei Oasis4_0 oder Oreocast sein");
            return true;
        }

        Location location = player.getLocation();

        Firework fireWork = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.PURPLE).build());
        fireworkMeta.setPower(3);

        fireWork.setFireworkMeta(fireworkMeta);
        return true;
    }
}
