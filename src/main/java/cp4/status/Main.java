package cp4.status;

import cp4.status.command.*;
import cp4.status.feature.AFKRankFeature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;


public final class Main extends JavaPlugin implements Listener {

    private File userDataFolder;

    Scoreboard sb;

    @Override
    public void onEnable() {

        File dataFolder = getDataFolder();
        userDataFolder = new File(dataFolder, "userdata");
        userDataFolder.mkdirs();
        File file = new File(getDataFolder(), "config.yml");


        sb = Bukkit.getScoreboardManager().getNewScoreboard();


        for (Role role : Role.values()) {
            role.registerTeam(sb);
        }

        //  Role.ADMIN.registerTeam(sb);
        //  Role.MOD.registerTeam(sb);
        //  Role.SUB.registerTeam(sb);
        //  Role.LIVE.registerTeam(sb);
        //  Role.AFK.registerTeam(sb);
        //  Role.USER.registerTeam(sb);

        this.getLogger().info("Start");

        ZeitCommand zeitCommand = new ZeitCommand(this);
        getCommand("Onlinezeit").setExecutor(zeitCommand);
        getServer().getPluginManager().registerEvents(zeitCommand, this);

        AFKRankFeature AFk_RankFeature = new AFKRankFeature((this));
        getCommand("Afk").setExecutor(AFk_RankFeature);
        getServer().getPluginManager().registerEvents(AFk_RankFeature, this);

        SubCommand SubCommand = new SubCommand((this));
        getCommand("Sub").setExecutor(SubCommand);


        ResetCommand reset = new ResetCommand();
        getCommand("reset").setExecutor(reset);

        FlameCommand flame = new FlameCommand(this);
        getCommand("flame").setExecutor(flame);

        Rang_Zuordnung Rang_Zuordnung = new Rang_Zuordnung(this);
        getServer().getPluginManager().registerEvents(Rang_Zuordnung, this);

        Streamer_commands Streamer_commands = new Streamer_commands(this);
        getCommand("live").setExecutor(Streamer_commands);

        OasisCommand OasisCommand = new OasisCommand(this);
        getCommand("Oasislive").setExecutor(OasisCommand);

        SpawnFireworkCommand SpawnFireworkCommand = new SpawnFireworkCommand();
        getCommand("rw").setExecutor(SpawnFireworkCommand);


        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new listeners(this), this);
        Bukkit.getPluginManager().registerEvents(new Rang_Zuordnung(this), this);

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

