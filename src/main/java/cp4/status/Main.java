package cp4.status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

      //  for(Role role : Role.values()) {
       //     role.registerTeam(sb);
     //   }

      //  Role.ADMIN.registerTeam(sb);
      //  Role.MOD.registerTeam(sb);
      //  Role.SUB.registerTeam(sb);
      //  Role.LIVE.registerTeam(sb);
      //  Role.AFK.registerTeam(sb);
      //  Role.USER.registerTeam(sb);

        Team team = sb.registerNewTeam("000001Admin");
        team.setPrefix("§4Admin §7| §4");
       team.setColor(ChatColor.DARK_RED);

      team = sb.registerNewTeam("000000AFK");
       team.setPrefix("§cAFK §7| §c");
        team.setColor(ChatColor.RED);

       team = sb.registerNewTeam("000002Mod");
        team.setPrefix("§6Mod §7| §6");
       team.setColor(ChatColor.GOLD);

       team = sb.registerNewTeam("000003Sub");
       team.setPrefix("§5Sub §7| §5");
       team.setColor(ChatColor.DARK_PURPLE);

       team = sb.registerNewTeam("000005Spieler");
       team.setPrefix("§8Spieler §7| §7");
        team.setColor(ChatColor.GRAY);

        team = sb.registerNewTeam("000006Streamer");
        team.setPrefix("§3Streamer §7| §3");
        team.setColor(ChatColor.RED);

       team = sb.registerNewTeam("000004Live");
        team.setPrefix("§9Live §7| §7");
        team.setColor(ChatColor.DARK_AQUA);


        this.getLogger().info("Start");

        ZeitCommand zeitCommand = new ZeitCommand(this);
        getCommand("Onlinezeit").setExecutor(zeitCommand);
        getServer().getPluginManager().registerEvents(zeitCommand, this);

        AFk_Rang AFk_Rang = new AFk_Rang((this));
        getCommand("Afk").setExecutor(AFk_Rang);
        getServer().getPluginManager().registerEvents(AFk_Rang, this);

        Sub Sub = new Sub((this));
        getCommand("Sub").setExecutor(Sub);


        reset reset = new reset((this));
        getCommand("reset").setExecutor(reset);

        flame flame = new flame(this);
        getCommand("flame").setExecutor(flame);

        Rang_Zuordnung Rang_Zuordnung = new Rang_Zuordnung(this);
        getServer().getPluginManager().registerEvents(Rang_Zuordnung, this);

        Streamer_commands Streamer_commands = new Streamer_commands(this);
        getCommand("live").setExecutor(Streamer_commands);

        oasis oasis = new oasis(this);
        getCommand("Oasislive").setExecutor(oasis);

        rw rw = new rw(this);
        getCommand("rw").setExecutor(rw);


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

        if (player.hasPermission("cp4.admin")) {
            team = "000001Admin";
        } else if (player.hasPermission("cp4.mod")) {
            team = "000002Mod";
        } else if (player.hasPermission("cp4.sub")) {
            team = "000003Sub";
        } else if (player.hasPermission("cp4.streamer")) {
            team = "000006Streamer";
        } else {
            team = "000004Spieler";
        }
        return sb.getTeam(team);
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

