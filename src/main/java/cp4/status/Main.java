package cp4.status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

        Objective obj = sb.registerNewObjective("deathCount", "deathCount");
        obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);


        Team team = sb.registerNewTeam("000001Admin");
        team.setPrefix("§4Admin §7| §4");
        team.setColor(ChatColor.RED);

        team = sb.registerNewTeam("000000AFK");
        team.setPrefix("§cAFK §7| §c");
        team.setColor(ChatColor.RED);

        team = sb.registerNewTeam("000002Mod");
        team.setPrefix("§4Mod §7| §4");
        team.setColor(ChatColor.GOLD);

        team = sb.registerNewTeam("000003Sup");
        team.setPrefix("§5Sup §7| §5");
        team.setColor(ChatColor.DARK_PURPLE);

        team = sb.registerNewTeam("000005Spieler");
        team.setPrefix("§8Spieler §7| §7");
        team.setColor(ChatColor.GRAY);

        team = sb.registerNewTeam("000004Live");
        team.setPrefix("§9Live §7| §7");



        this.getLogger().info("Start");

        ZeitCommand zeitCommand = new ZeitCommand(this);
        getCommand("Onlinezeit").setExecutor(zeitCommand);
        getServer().getPluginManager().registerEvents(zeitCommand, this);

        AFk_Rang AFk_Rang = new AFk_Rang((this));
        getCommand("Afk").setExecutor(AFk_Rang);
        getServer().getPluginManager().registerEvents(AFk_Rang, this);

        Rang_upgreat Rang_upgreat = new Rang_upgreat((this));
        getCommand("Test").setExecutor(Rang_upgreat);

        flame flame = new flame(this);
        getCommand("flame").setExecutor(flame);

        Rang_Zuordnung Rang_Zuordnung = new Rang_Zuordnung(this);
        getServer().getPluginManager().registerEvents(Rang_Zuordnung, this);

        Streamer_commands Streamer_commands = new Streamer_commands(this);
        getCommand("live").setExecutor(Streamer_commands);



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
        String team;

        if (player.isOp()) {
            team = "000001Admin";
        } else if (player.hasPermission("bukkit.broadcast")) {
            team = "000002Mod";
        } else if (player.hasPermission("server.mod")) {
            team = "000003Sub";
        } else {
            team = "000004Spieler";
        }
        return sb.getTeam(team);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(sb); // Set the scoreboard to the player when the player joins the server
        setPrefix(player); // Selects the team of the permission group and adds the player to the group
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getTeamOfPlayer(player).removeEntry(player.getName()); // Removes the player from the team when the player leaves the server
    }


}

