package me.wiviw.fantastick.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static me.wiviw.fantastick.Fantastick.CommandPerm;
import static me.wiviw.fantastick.Fantastick.Pluginname;
import static me.wiviw.fantastick.utils.Utils.*;

public class teleportSticks implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String Permission = Pluginname + "." + CommandPerm + "." + "teleportsticks";
        switch (getCommandModeAndCheckPerm(sender, Permission)) {
            case 0: // Do not execute
            case 2: // Execute as Other
                return false;
            case 1: // Execute as player
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Specify Target(s), type, destination, and optionally a destination user");
                    return false;
                } else {
                    //Variables to set
                    String type;
                    String target;
                    String destination;
                    String destinationuser;
                    //Check for Arg0
                    List<String> arg0CheckList = new ArrayList<>();
                    arg0CheckList.add("Player");
                    arg0CheckList.add("Team");
                    arg0CheckList.add("Gamemode");
                    arg0CheckList.add("Everyone");
                    if (!(checkArg(arg0CheckList, args[0]))) {
                        sender.sendMessage(ChatColor.RED + "Specify Type");
                        return false;
                    } else {
                        type = args[0];
                    }
                    List<String> arg1CheckList = new ArrayList<>();
                    arg1CheckList.addAll(getAllPlayers(sender));
                    arg1CheckList.addAll(getAllTeams(sender));
                    arg1CheckList.add("spectator");
                    arg1CheckList.add("creative");
                    arg1CheckList.add("survival");
                    arg1CheckList.add("adventure");
                    arg1CheckList.add("@a");
                    if (!(checkArg(arg1CheckList, args[1]))) {
                        sender.sendMessage(ChatColor.RED + "Specify Target(s)");
                        return false;
                    } else {
                        target = args[1];
                    }
                    List<String> arg2CheckList = new ArrayList<>();
                    arg2CheckList.add("Block");
                    arg2CheckList.add("User");
                    if (!(checkArg(arg2CheckList, args[2]))) {
                        sender.sendMessage(ChatColor.RED + "Specify Destination");
                        return false;
                    } else {
                        destination = args[2];
                    }
                    if (args.length > 3) {
                        Player p = sender.getServer().getPlayer(args[3]);
                        if (args[2].equals("User")) {
                            if (p!=null){
                                destinationuser = args[3];
                                giveUserStick((Player) sender, type, target, destination, destinationuser);
                            }else{
                                sender.sendMessage(ChatColor.RED + "Specify Destination User");
                                return false;
                            }
                        }
                    } else {
                        giveUserStick((Player) sender, type, target, destination);
                    }
                    return true;
                }
        }
        return false;
    }

    public boolean checkArg(List<String> allowedArgs, String checkedArg) {
        for (String s : allowedArgs) {
            if (s.equals(checkedArg)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> TargetAutoComplete = new ArrayList<>();
        // Type
        if (args.length < 2) {
            TargetAutoComplete.add("Player");
            TargetAutoComplete.add("Team");
            TargetAutoComplete.add("Gamemode");
            TargetAutoComplete.add("Everyone");
            return TargetAutoComplete;
        }
        if (args.length < 3) {
            switch (args[0]) {
                case "Player":
                    TargetAutoComplete.addAll(getAllPlayers(sender));
                    return TargetAutoComplete;
                case "Team":
                    TargetAutoComplete.addAll(getAllTeams(sender));
                    return TargetAutoComplete;
                case "Gamemode":
                    TargetAutoComplete.add("spectator");
                    TargetAutoComplete.add("creative");
                    TargetAutoComplete.add("survival");
                    TargetAutoComplete.add("adventure");
                    return TargetAutoComplete;
                case "Everyone":
                    TargetAutoComplete.add("@a");
                    return TargetAutoComplete;
            }
        }
        if (args.length < 4) {
            TargetAutoComplete.add("Block");
            TargetAutoComplete.add("User");
            return TargetAutoComplete;
        }
        if (args.length < 5) {
            if (args[2].equals("User")) {
                TargetAutoComplete.addAll(getAllPlayers(sender));
            } else {
                TargetAutoComplete.add("");
            }
            return TargetAutoComplete;
        }
        TargetAutoComplete.add("");
        return TargetAutoComplete;
    }

    public void giveUserStick(Player p, String type, String target, String destination) {
        giveUserStick(p, type, target, destination, null);
    }

    public void giveUserStick(Player p, String type, String target, String destination, String destinationUser) {
        ItemStack stick = getTeleportSticks(p, type, target, destination, destinationUser);
        giveItemToPlayer(p, stick);
    }

    public ItemStack getTeleportSticks(Player p, String type, String target, String destination, String destinationUser) {
        NBTItem nbti = new NBTItem(new ItemStack(Material.STICK));
        JsonObject CustomTags = new JsonObject();
        // Enchant Storage
        JsonObject AbilityStorage = new JsonObject();
        AbilityStorage.addProperty("Enchant", "teleport_stick");
        CustomTags.add("AbilityStorage", AbilityStorage);
        // Setting up stick
        JsonObject TeleportInfo = new JsonObject();
        TeleportInfo.addProperty("teleType", type);
        TeleportInfo.addProperty("teleTarget", target);
        TeleportInfo.addProperty("teleDestination", destination);
        if (destination.equals("User")){
            TeleportInfo.addProperty("teleDestinationUser", destinationUser);
        }else{
            Location loc = p.getLocation();
            JsonObject teleDestinationLocationInfo = new JsonObject();
            teleDestinationLocationInfo.addProperty("world", loc.getWorld().getName()); // Deal with world later
            teleDestinationLocationInfo.addProperty("x", loc.getX());
            teleDestinationLocationInfo.addProperty("y", loc.getY());
            teleDestinationLocationInfo.addProperty("z", loc.getZ());
            teleDestinationLocationInfo.addProperty("pitch", loc.getPitch());
            teleDestinationLocationInfo.addProperty("yaw", loc.getYaw());
            TeleportInfo.add("teleDestinationLocationInfo", teleDestinationLocationInfo);
        }
        CustomTags.add("TeleportInfo", TeleportInfo);
        //Final setting of custom tags
        nbti.setObject("CustomTags", CustomTags);
        //Setting Other Meta, Item Name + Lore
        ItemStack stick = nbti.getItem();
        // Adjust Stick Before NBT edit
        ItemMeta futureMeta = stick.getItemMeta();
        futureMeta.setDisplayName(ChatColor.GOLD + "Teleport Stick");
        List<String> lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "Teleport Type: " + ChatColor.WHITE + type);
        lore.add(ChatColor.GRAY + "Target: " + ChatColor.WHITE + target);
        lore.add(ChatColor.GRAY + "Destination: " + ChatColor.WHITE + destination);
        if (destinationUser!=null){
            lore.add(ChatColor.GRAY + "Destination User: " + ChatColor.WHITE + destinationUser);
        }else{
            //lore.add(ChatColor.GRAY + "Destination Block: " + ChatColor.WHITE + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ());
        }
        lore.add(ChatColor.GOLD + "Created By User: " + ChatColor.WHITE + p.getName());
        futureMeta.setLore(lore);
        stick.setItemMeta(futureMeta);
        return stick;
    }
}
