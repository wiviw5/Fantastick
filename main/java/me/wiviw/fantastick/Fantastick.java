package me.wiviw.fantastick;

import me.wiviw.fantastick.commands.*;
import me.wiviw.fantastick.events.InteractionListener;
import me.wiviw.fantastick.events.RespawnListener;
import me.wiviw.fantastick.events.WorldGuardFlagButNot;
import me.wiviw.fantastick.events.chatListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fantastick extends JavaPlugin {

    public static Plugin MainPlugin;
    public static String Pluginname = "fantastick";
    public static String CommandPerm = "command";

    @Override
    public void onEnable() {
        MainPlugin = this;
        getServer().getPluginManager().registerEvents(new InteractionListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new WorldGuardFlagButNot(), this);
        //getServer().getPluginManager().registerEvents(new chatListener(), this);
        getCommand("invistoggle").setExecutor(new invistoggle());
        getCommand("nightvisiontoggle").setExecutor(new nightVisionToggle());
        getCommand("waterbreathingtoggle").setExecutor(new waterBreathingToggle());
        getCommand("teleportsticks").setExecutor(new teleportSticks());
        getCommand("deathsticks").setExecutor(new deathsticks());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
