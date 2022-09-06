package me.wiviw.fantastick.commands;

import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.wiviw.fantastick.Fantastick.CommandPerm;
import static me.wiviw.fantastick.Fantastick.Pluginname;
import static me.wiviw.fantastick.utils.Utils.*;

public class deathsticks implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String Permission = Pluginname + "." + CommandPerm + "." + "deathsticks";
        switch (getCommandModeAndCheckPerm(sender, Permission)) {
            case 0: // Do not execute
                return false;
            case 1: // Execute as player
                if (args.length < 1) {
                    giveItemToPlayer((Player) sender, getDeathStick()); // With no args it only applies to themself
                } else {
                    if (args[0].equals("@a")) {
                        for (Player p : sender.getServer().getOnlinePlayers()) {
                            giveItemToPlayer(p, getDeathStick());
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
                return true;
            case 2: // Execute as Other
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "You must have some arguments specifying who you want to give this to.");
                    return false;
                } else {
                    if (args[0].equals("@a")) {
                        for (Player p : sender.getServer().getOnlinePlayers()) {
                            giveItemToPlayer(p, getDeathStick());
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(args[0].length() > 0)) {
            List<String> playerAndSelectors = getSelectors();
            for (Player p : sender.getServer().getOnlinePlayers()) {
                playerAndSelectors.add(p.getName());
            }
            return playerAndSelectors;
        } else {
            return new ArrayList<>();
        }
    }

    public static ItemStack getDeathStick() {
        ItemStack DeathStick = new ItemStack(Material.STICK);
        ItemMeta DeathStickMeta = DeathStick.getItemMeta();
        DeathStickMeta.setDisplayName(ChatColor.RED + "Death Stick");
        DeathStick.setItemMeta(DeathStickMeta);
        NBTItem nbti = new NBTItem(DeathStick);
        JsonObject CustomTags = new JsonObject();
        //Below is Enchant Adding
        JsonObject AbilityStorage = new JsonObject();
        AbilityStorage.addProperty("Enchant", "death_stick");
        CustomTags.add("AbilityStorage", AbilityStorage);
        //Custom Tag Adding
        nbti.setObject("CustomTags", CustomTags);
        return nbti.getItem();
    }
}
