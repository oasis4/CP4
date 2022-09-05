package cp4.status;

import cp4.status.command.*;
import cp4.status.feature.AFKRankFeature;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;


public final class CP4Plugin extends JavaPlugin implements Listener {

    private File userDataFolder;

    Scoreboard sb;

    @Override
    public void onEnable() {

        userDataFolder = new File(getDataFolder(), "userdata");
        userDataFolder.mkdirs();
        File file = new File(getDataFolder(), "config.yml");


        sb = Bukkit.getScoreboardManager().getNewScoreboard();

        for (Role role : Role.values()) {
            role.registerTeam(sb);
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setScoreboard(sb);
            setPrefix(onlinePlayer);
        }


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


    private void setPrefix(Player p) {
        Team sbTeam = getTeamOfPlayer(p);
        sbTeam.addEntry(p.getName());
        p.setDisplayName(sbTeam.getPrefix() + p.getName());

        //String Rolle = String.valueOf(sb.getTeam(team));

    }

    public Team getTeamOfPlayer(Player player) {
        Role role = Role.USER;

        if (player.hasPermission("cp4.admin")) {
            role = Role.ADMIN;
        } else if (player.hasPermission("cp4.mod")) {
            role = Role.MOD;
        } else if (player.hasPermission("cp4.sub")) {
            role = Role.SUB;
        } else if (player.hasPermission("cp4.streamer")) {
            role = Role.STREAMER;
        }
        return sb.getTeam(role.getId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        player.sendTitle("§3Willkommen", "", 1, 20, 1);
        player.setScoreboard(sb); // Set the scoreboard to the player when the player joins the server
        setPrefix(player); // Selects the team of the permission group and adds the player to the group
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getTeamOfPlayer(player).removeEntry(player.getName()); // Removes the player from the team when the player leaves the server
    }


}

