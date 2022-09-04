package cp4.status;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public enum Role {

    ADMIN("000001Admin", "§4Admin §7| §4", ChatColor.RED),
    MOD("000002Mod", "§6Mod §7| §6", ChatColor.GOLD),
    SUB("000003Sub", "§5Sub §7| §5", ChatColor.DARK_PURPLE),
    USER("000004Spieler", "§8Spieler §7| §7", ChatColor.GRAY),
    STREAMER("000006Streamer", "§3Streamer §7| §3", ChatColor.RED),
    LIVE("000005Live", "§9Live §7| §7", ChatColor.DARK_AQUA),
    AFK("000000AFK", "§cAFK §7| §c", ChatColor.RED);

    private final String id;
    private final String prefix;
    private final ChatColor color;

    Role(String id, String prefix, ChatColor color) {
        this.id = id;
        this.prefix = prefix;
        this.color = color;
    }

    public Team registerTeam(Scoreboard board) {
        Team team = board.registerNewTeam(id);
        team.setPrefix(prefix);
        team.setColor(color);
        return team;
    }

    public String getId() {
        return id;
    }
}
