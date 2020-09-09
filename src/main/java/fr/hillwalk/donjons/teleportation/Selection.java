package fr.hillwalk.donjons.teleportation;


import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class Selection {
        private List<Location> locationPortals = new ArrayList<Location>();

        public void selectedArea(Location loc1, Location loc2){
            Location pos1 = new Location(UtilsRef.principalWorld(), loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ());
            Location pos2 = new Location(UtilsRef.principalWorld(), loc2.getBlockX(), loc2.getBlockY() , loc2.getBlockZ());

            double minx = Math.min(pos1.getX(), pos2.getX());
            double maxx = Math.max(pos1.getX(), pos2.getX());
            double miny = Math.min(pos1.getY(), pos2.getY());
            double maxy = Math.max(pos1.getY(), pos2.getY());
            double minz = Math.min(pos1.getZ(), pos2.getZ());
            double maxz = Math.max(pos1.getZ(), pos2.getZ());
            for (double x = minx; x <= maxx; x++) {
                for (double y = miny; y <= maxy; y++) {
                    for (double z = minz; z <= maxz; z++) {

                         if (!(x == minx || x == maxx || z == minz || z == maxz || y == miny || y == maxy)) {
                              continue;
                         } else {
                             Location loc = new Location(UtilsRef.principalWorld(), x, y, z);
                             locationPortals.add(loc);
                             loc.getBlock().setType(Material.NETHER_PORTAL);
                        }
                    }
                }
            }

        }

}