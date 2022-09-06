package me.wiviw.fantastick.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.wiviw.fantastick.Fantastick.CommandPerm;
import static me.wiviw.fantastick.Fantastick.Pluginname;
import static me.wiviw.fantastick.events.RespawnListener.respawnwithInvis;
import static me.wiviw.fantastick.utils.Utils.getCommandModeAndCheckPerm;

public class invistoggle implements TabExecutor {

    public PotionEffect Invis = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE,0,false,false);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String Permission = Pluginname + "." + CommandPerm + "." + "invistoggle";
        switch(getCommandModeAndCheckPerm(sender, Permission)){
            case 0: // Do not execute
                return false;
            case 1: // Execute as player
            case 2: // Execute as Other
                if (args.length<1){
                    if (respawnwithInvis){
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.RED + " off " + ChatColor.GRAY + "invisibility for players");
                        respawnwithInvis=false;
                        removeInvis(sender);
                    }else{
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.GREEN + " on " + ChatColor.GRAY + "invisibility for players");
                        addInvis(sender);
                        respawnwithInvis=true;
                    }
                }else{
                    if (args[0].equals("true")){
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.GREEN + " on " + ChatColor.GRAY + "invisibility for players");
                        addInvis(sender);
                        respawnwithInvis=true;
                    }else{
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.RED + " off " + ChatColor.GRAY + "invisibility for players");
                        removeInvis(sender);
                        respawnwithInvis=false;
                    }
                }
                return true;
        }
        return false;
    }

    private void removeInvis(CommandSender sender){
        for (Player player : sender.getServer().getOnlinePlayers()){
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    private void addInvis(CommandSender sender){
        for (Player player : sender.getServer().getOnlinePlayers()){
            player.addPotionEffect(Invis);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(args[0].length()>0)){
            return Arrays.asList("true", "false");
        }else{
            return new ArrayList<>();
        }
    }
}
