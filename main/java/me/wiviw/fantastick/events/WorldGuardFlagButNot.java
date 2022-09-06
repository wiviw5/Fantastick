package me.wiviw.fantastick.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class WorldGuardFlagButNot implements Listener {

    @EventHandler
    public void FlagButNot(ProjectileLaunchEvent e) {
        Location loc = e.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World wrld = BukkitAdapter.adapt(loc.getWorld());
        RegionManager regions = container.get(wrld);
        BlockVector3 position = BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (regions != null) {
            ApplicableRegionSet set = regions.getApplicableRegions(position);
            for (ProtectedRegion r : set) {
                if (r.getId().equals("cancelProjectileLaunches")) {
                    e.setCancelled(true);
                }
            }
        }
    }

}
