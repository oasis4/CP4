package cp4.status;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public enum Role {

    ADMIN("000001Admin", "§4Admin §7| §4", ChatColor.RED),
    MOD("000002Mod", "§6Mod §7| §6", ChatColor.GOLD),
    SUB,
    USER,
    LIVE,
    AFK;


    private final String id;
    private final String prefix;
    private final ChatColor color;

    public Role(String id, String prefix, ChatColor color) {
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

}
