package com.hotmail.shinyclef.shinydynamics;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Author: ShinyClef
 * Date: 25/10/12
 * Time: 3:48 AM
 */

public class LocationManager
{
    private static ShinyDynamics plugin = ShinyDynamics.getPlugin();
    private static World world = plugin.getServer().getWorld("world");

    private static Location danielleStartLoc = new Location(world, -17, 64, -8);


    public static Location getDanielleStartLoc()
    {
        return danielleStartLoc;
    }
}
