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
import static me.wiviw.fantastick.events.RespawnListener.respawnwithWaterBreathing;
import static me.wiviw.fantastick.utils.Utils.getCommandModeAndCheckPerm;

public class waterBreathingToggle implements TabExecutor {

    public PotionEffect WaterBreathing = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String Permission = Pluginname + "." + CommandPerm + "." + "waterbreathingtoggle";
        switch (getCommandModeAndCheckPerm(sender, Permission)) {
            case 0: // Do not execute
                return false;
            case 1: // Execute as player
            case 2: // Execute as Other
                if (args.length < 1) {
                    if (respawnwithWaterBreathing) {
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.RED + " off " + ChatColor.GRAY + "water breathing for players");
                        respawnwithWaterBreathing = false;
                        removeWaterBreathing(sender);
                    } else {
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.GREEN + " on " + ChatColor.GRAY + "water breathing for players");
                        addWaterBreathing(sender);
                        respawnwithWaterBreathing = true;
                    }
                } else {
                    if (args[0].equals("true")) {
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.GREEN + " on " + ChatColor.GRAY + "water breathing for players");
                        addWaterBreathing(sender);
                        respawnwithWaterBreathing = true;
                    } else {
                        sender.sendMessage(ChatColor.GRAY + "Turned" + ChatColor.RED + " off " + ChatColor.GRAY + "water breathing for players");
                        removeWaterBreathing(sender);
                        respawnwithWaterBreathing = false;
                    }
                }
                return true;
        }
        return false;
    }

    private void removeWaterBreathing(CommandSender sender) {
        for (Player player : sender.getServer().getOnlinePlayers()) {
            player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        }
    }

    private void addWaterBreathing(CommandSender sender) {
        for (Player player : sender.getServer().getOnlinePlayers()) {
            player.addPotionEffect(WaterBreathing);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(args[0].length() > 0)) {
            return Arrays.asList("true", "false");
        } else {
            return new ArrayList<>();
        }
    }
}
