package me.wiviw.fantastick.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void giveItemToPlayer(Player p, ItemStack i){
        int firstEmpty = p.getInventory().firstEmpty();
        if (firstEmpty==-1){
            p.sendMessage(ChatColor.RED + "You had a full inventory, so I dropped your item on the ground.");
            p.getWorld().dropItem(p.getLocation(), i);
        }else{
            p.getInventory().setItem(firstEmpty, i);
        }
    }
    public static List<String> getSelectors(){
        List<String> Selectors = new ArrayList<>();
        Selectors.add("@a");
        Selectors.add("@p");
        Selectors.add("@s");
        return Selectors; // @a = ALl Players, @p Closest Player, @s Entity Who executed the command
    }

    public static List<String> getAllTeams(CommandSender sender){
        List<String> Teams = new ArrayList<>();
        for (Team t : sender.getServer().getScoreboardManager().getMainScoreboard().getTeams()){
            Teams.add(t.getName());
        }
        return Teams;
    }

    public static List<String> getAllPlayers(CommandSender sender){
        List<String> Players = new ArrayList<>();
        for (Player p : sender.getServer().getOnlinePlayers()){
            Players.add(p.getName());
        }
        return Players;
    }

    public static List<Player> getAllPlayersOnTeam(Player p, Team t){
        List<Player> Players = new ArrayList<>();
        for (String player : t.getEntries()){
            if (p.getServer().getPlayer(player)!=null){
                Players.add(p.getServer().getPlayer(player));
            }
        }
        return Players;
    }

    /*
    Modes:
    0 = Do not execute
    1 = Execute with player
    2 = Execute from other source
     */
    public static int getCommandModeAndCheckPerm(CommandSender sender, String permission){
        if (sender instanceof Player){
            if (sender.hasPermission(permission) || sender.isOp()){
                return 1;
            }else{
                sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
                return 0;
            }
        }else{
            return 2;
        }
    }
}
