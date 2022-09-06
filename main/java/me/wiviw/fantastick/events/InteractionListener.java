package me.wiviw.fantastick.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.*;

import static me.wiviw.fantastick.utils.Utils.*;

public class InteractionListener implements Listener {
    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        checkAndActivateItemAbility(e.getItem(), e.getPlayer());
    }


    @EventHandler
    public void interactPVPEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            checkAndActivateItemAbility(p.getInventory().getItemInMainHand(), p);
        }
    }

    public void checkAndActivateItemAbility(ItemStack item, Player player) {
        if (item != null) {
            if (item.hasItemMeta()) {
                NBTItem nbti = new NBTItem(item);
                if (nbti.hasKey("CustomTags")) {
                    if (nbti.getObject("CustomTags", JsonObject.class).getAsJsonObject("AbilityStorage").has("Enchant")) {
                        String itemName = nbti.getObject("CustomTags", JsonObject.class).getAsJsonObject("AbilityStorage").get("Enchant").getAsString();
                        switch (itemName) {
                            case "death_stick":
                                deathStickActivate(player);
                                break;
                            case "teleport_stick":
                                teleportStickActivate(player, nbti);
                                break;
                        }
                    }
                }
            }
        }
    }

    public void deathStickActivate(Player activator) {
        activator.setHealth(0);
    }

    public void teleportStickActivate(Player activator, NBTItem nbti) {
        JsonObject CustomTags = nbti.getObject("CustomTags", JsonObject.class);
        JsonObject TeleportInfo = CustomTags.getAsJsonObject("TeleportInfo");
        String type = TeleportInfo.getAsJsonPrimitive("teleType").getAsString();
        String target = TeleportInfo.getAsJsonPrimitive("teleTarget").getAsString();
        List<Player> teleportList = new ArrayList<>();
        switch (type){
            case "Player":
                teleportList.add(activator.getServer().getPlayer(target));
                break;
            case "Team":
                Team t = activator.getScoreboard().getTeam(target);
                teleportList = getAllPlayersOnTeam(activator, t);
                break;
            case "Gamemode":
                for (Player p : activator.getServer().getOnlinePlayers()){
                    if (p.getGameMode().name().equals(target.toUpperCase())){
                        teleportList.add(p);
                    }
                }
                break;
            case "Everyone":
                teleportList.addAll(activator.getServer().getOnlinePlayers());
                break;
        }
        String destination = TeleportInfo.getAsJsonPrimitive("teleDestination").getAsString();
        if (destination.equals("User")){
            String destinationUserStr = TeleportInfo.getAsJsonPrimitive("teleDestinationUser").getAsString();
            Player destinationUser = activator.getServer().getPlayer(destinationUserStr);
            for (Player p : teleportList){
                p.teleport(destinationUser.getLocation());
                activator.sendMessage(ChatColor.GRAY + "Teleported: " + p.getName() + " to that person.");
            }
        }else{
            JsonObject teleDestinationLocationInfo = TeleportInfo.getAsJsonObject("teleDestinationLocationInfo");
            String world = teleDestinationLocationInfo.getAsJsonPrimitive("world").getAsString();
            double x = teleDestinationLocationInfo.getAsJsonPrimitive("x").getAsDouble();
            double y = teleDestinationLocationInfo.getAsJsonPrimitive("y").getAsDouble();
            double z = teleDestinationLocationInfo.getAsJsonPrimitive("z").getAsDouble();
            float pitch = teleDestinationLocationInfo.getAsJsonPrimitive("pitch").getAsFloat();
            float yaw = teleDestinationLocationInfo.getAsJsonPrimitive("yaw").getAsFloat();
            Location teleportTo = new Location(activator.getServer().getWorld(world), x,y,z,yaw,pitch);
            for (Player p : teleportList){
                p.teleport(teleportTo);
                activator.sendMessage(ChatColor.GRAY + "Teleported: " + p.getName() + " to that block.");
            }
        }
    }

    /*
        JsonArray TeleportList = nbti.getObject("CustomTags", JsonObject.class).getAsJsonArray("TeleportList");
        for (JsonElement test : TeleportList) {
            JsonObject Object = test.getAsJsonObject();
            for (Map.Entry<String, JsonElement> ListValue : Object.entrySet()) {
                String PlayerUUID = Object.get(ListValue.getKey()).toString().replace("\"", "");
                Player x = Bukkit.getPlayer(UUID.fromString(PlayerUUID));
                if (x != null) {
                    x.teleport(activator.getLocation());
                    activator.sendMessage(ChatColor.GRAY + ListValue.getKey() + ChatColor.GRAY + " has been teleported!");
                }
            }
        }
     */
}
