package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.api.CraftProtect;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.storage.AsyncUserStorage;
import com.github.oasis.craftprotect.utils.M;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class UserCommand implements CraftProtectCommand {

    @Inject
    private CraftProtect protect;

    @Inject
    private AsyncUserStorage storage;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.user";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            protect.sendMessage(sender, M.NO_PLAYER);
            return true;
        }

        if (args.length == 0) {
            storage.findUserAsync(player.getUniqueId())
                    .thenAccept(craftProtectUser -> {
                        craftProtectUser.ifPresentOrElse(craftProtectUser1 -> {
                            sender.sendMessage(craftProtectUser1.getId().toString() + ": ");
                            sender.sendMessage("   Twitch: " + craftProtectUser1.getTwitchId());
                            sender.sendMessage("   Discord: " + craftProtectUser1.getDiscordId());
                        }, () -> player.sendMessage("Du wurdest nicht in der Datenbank gefunden"));
                    });
            return true;
        }

        if (args.length != 1)
            return false;

        String name = args[0];
        Player target = Bukkit.getPlayer(name);
        if (target == null) {
            sender.sendMessage("§cPlayer not found");
            return true;
        }

        storage.findUserAsync(target.getUniqueId())
                .thenAccept(craftProtectUser -> {
                    craftProtectUser.ifPresentOrElse(craftProtectUser1 -> {
                        sender.sendMessage(craftProtectUser1.getId().toString() + ": ");
                        sender.sendMessage("   Twitch: " + craftProtectUser1.getTwitchId());
                        sender.sendMessage("   Discord: " + craftProtectUser1.getDiscordId());
                    }, () -> player.sendMessage(target.getName() + " wurde nicht in der Datenbank gefunden"));
                });
        return true;
    }
}
