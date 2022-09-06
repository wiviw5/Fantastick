package me.wiviw.fantastick.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Team;

public class chatListener implements Listener {

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        String PlayerFormat = getTeamColor(e.getPlayer()) + getTeamPrefix(e.getPlayer()) + e.getPlayer().getPlayerListName();
        e.setFormat(PlayerFormat + ChatColor.WHITE + ": " + e.getMessage());
    }

    public static ChatColor getTeamColor(Player p) {
        for (Team t : p.getScoreboard().getTeams()) {
            for (String name : t.getEntries()) {
                if (p.getName().equals(name)) {
                    return t.getColor();
                }
            }
        }
        return ChatColor.WHITE;
    }

    public static String getTeamPrefix(Player p) {
        for (Team t : p.getScoreboard().getTeams()) {
            for (String name : t.getEntries()) {
                if (p.getName().equals(name)) {
                    return t.getPrefix();
                }
            }
        }
        return "";
    }
}
