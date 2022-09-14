package cp4.status;

import cp4.status.command.*;
import cp4.status.command.ZeitCommand;
import cp4.status.feature.AFKRankFeature;
import de.lebaasti.craftprotect4.CraftProtect4Kt;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class CP4Plugin extends JavaPlugin implements Listener {

    private File userDataFolder;

    @Override
    public void onEnable() {

        userDataFolder = new File(getDataFolder(), "userdata");
        userDataFolder.mkdirs();
        File file = new File(getDataFolder(), "config.yml");


        this.getLogger().info("Start");

        ZeitCommand zeitCommand = new ZeitCommand(this);
        registerCommand("onlinezeit", zeitCommand);
        getServer().getPluginManager().registerEvents(zeitCommand, this);

        AFKRankFeature afkRankFeature = new AFKRankFeature(this);
        registerCommand("afk", afkRankFeature);
        getServer().getPluginManager().registerEvents(afkRankFeature, this);


        registerCommand("sub", new SubCommand());
        registerCommand("reset", new ResetCommand());
        registerCommand("flame", new FlameCommand(this));
        registerCommand("live", new StreamerCommands(this));
        registerCommand("Oasislive", new OasisCommand(this));
        registerCommand("rw", new SpawnFireworkCommand());


        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Bukkit.getPluginManager().registerEvents(new RangZuordnung(this), this);
        Bukkit.getPluginManager().registerEvents(new emoji(this), this);
        CraftProtect4Kt.registerEvents();

    }

    private boolean registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if(command == null)
            return false;

        command.setExecutor(executor);

        if(executor instanceof TabCompleter) {
            command.setExecutor(executor);
        }
        return true;
    }

    @Override
    public void onDisable() {
        this.getLogger().info("ENDE");

    }

    public File getUserDataFolder() {
        return this.userDataFolder;
    }



    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendTitle("§3Willkommen", "", 1, 20, 1);
    }


}

