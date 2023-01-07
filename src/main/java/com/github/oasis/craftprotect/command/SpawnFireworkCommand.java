package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.utils.M;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

public class SpawnFireworkCommand implements CraftProtectCommand {

    private final CraftProtect protect;

    public SpawnFireworkCommand(CraftProtect protect) {
        this.protect = protect;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            protect.sendMessage(sender, M.NO_PLAYER);
            return true;
        }
        if (!(sender.hasPermission("cp4.sub"))) {
            protect.sendMessage(sender, M.NO_SUB);
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
