package me.wiviw.fantastick.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.wiviw.fantastick.Fantastick.MainPlugin;

public class RespawnListener implements Listener {

    public static boolean respawnwithInvis = false;
    public PotionEffect Invis = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false);
    public int delay = 5;

    @EventHandler
    public void onRespawnInvis(PlayerRespawnEvent e) {
        if (respawnwithInvis) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().addPotionEffect(Invis);
                }
            }.runTaskLater(MainPlugin, delay);
        }
    }

    public static boolean respawnwithNightVision = false;
    public PotionEffect NightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);

    @EventHandler
    public void onRespawnNightVision(PlayerRespawnEvent e) {
        if (respawnwithNightVision) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().addPotionEffect(NightVision);
                }
            }.runTaskLater(MainPlugin, delay);
        }
    }

    public static boolean respawnwithWaterBreathing = false;
    public PotionEffect WaterBreathing = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false);

    @EventHandler
    public void onRespawnWaterBreathing(PlayerRespawnEvent e) {
        if (respawnwithNightVision) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().addPotionEffect(WaterBreathing);
                }
            }.runTaskLater(MainPlugin, delay);
        }
    }
}
