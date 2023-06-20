package com.github.oasis.craftprotect.command;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.CraftProtectCommand;
import com.github.oasis.craftprotect.api.CraftProtectUser;
import com.github.oasis.craftprotect.controller.PlayerDisplayController;
import com.github.oasis.craftprotect.link.MinecraftExecution;
import com.github.oasis.craftprotect.link.TwitchExecution;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class LinkCommand implements CraftProtectCommand {

    @Inject
    private CraftProtectPlugin plugin;

    @Inject
    private PlayerDisplayController controller;

    @Override
    public @Nullable String getPermission() {
        return "cp.command.link";
    }

    @Override
    public String getUsage() {
        return "/<command> <twitch|discord>";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player))
            return false;

        if (args.length == 1 && args[0].equalsIgnoreCase("twitch")) {
            byte[] bytes = new byte[8];
            ThreadLocalRandom.current().nextBytes(bytes);
            BigInteger integer = new BigInteger(bytes);
            integer = integer.abs();

            String token = integer.toString(Character.MAX_RADIX);
            String uri = plugin.getCraftProtectConfig().getTwitch().formattedURI();
            player.sendMessage(uri.replace("{sessionId}", token));

            plugin.getAuthorizationCache().put(token, (TwitchExecution) (userId, live) -> {

                plugin.getUserStorage()
                        .findUserAsync(player.getUniqueId())
                        .thenApply(craftProtectUser -> {
                            CraftProtectUser user = craftProtectUser.orElseGet(() -> new CraftProtectUser(player.getUniqueId(), null, null));
                            user.setTwitchId(userId);
                            return user;
                        }).thenAccept(craftProtectUser -> {
                            plugin.getUserStorage().persist(craftProtectUser);
                        });

                if (live)
                    controller.update(player, model -> model.setLive(true));
            });

            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("minecraft")) {
            byte[] bytes = new byte[8];
            ThreadLocalRandom.current().nextBytes(bytes);
            BigInteger integer = new BigInteger(bytes);
            integer = integer.abs();

            String token = integer.toString(Character.MAX_RADIX);
            String uri = plugin.getCraftProtectConfig().getMinecraft().formattedURI();
            player.sendMessage(uri.replace("{sessionId}", token));
            plugin.getAuthorizationCache().put(token, (MinecraftExecution) id -> player.sendMessage("Id: " + id));
            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String
            label, @NotNull String[] args) {
        if (args.length <= 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("twitch", "discord", "minecraft"), new ArrayList<>());
        }
        return null;
    }
}
