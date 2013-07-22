package com.hotmail.shinyclef.shinydynamics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

/**
 * Author: ShinyClef
 * Date: 24/10/12
 * Time: 3:20 AM
 */
public class EventListener implements Listener
{
    private ShinyDynamics plugin;
    private static Set<String> moveEventPlayers;


    public EventListener(ShinyDynamics plugin, Set<String> moveEventPlayers)
    {
        //set fields
        this.plugin = plugin;
        this.moveEventPlayers = moveEventPlayers;

        //register plugin to receive events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void eventPlayerLogin(PlayerJoinEvent ev)
    {

    }

    @EventHandler
    public void eventPlayerMove(PlayerMoveEvent ev)
    {
        if (!moveEventPlayers.contains(ev.getPlayer().getName()))
        {
            return;
        }

        int fromX = (int) ev.getFrom().getX();
        int fromY = (int) ev.getFrom().getY();
        int fromZ = (int) ev.getFrom().getZ();
        int toX = (int) ev.getTo().getX();
        int toY = (int) ev.getTo().getY();
        int toZ = (int) ev.getTo().getZ();
        if (fromX == toX && fromY == toY && fromZ == toZ)
        {
            return;
        }

        HouseOfIllusions.moveEvent(ev.getPlayer());
    }

    @EventHandler
    public void onClick(PlayerInteractEvent ev)
    {
        if(ev.getAction() == Action.RIGHT_CLICK_BLOCK && ev.getClickedBlock().getType() == Material.STONE_BUTTON)
        {
            Location loc = ev.getClickedBlock().getLocation();
            if (loc.getWorld().getName().equals("world") &&
                    loc.getX() == 174 && loc.getY() == 62 && loc.getZ() == -125)
            {
                HouseOfIllusions.buttonPress(ev.getPlayer());
            }
        }
    }

    public static Set<String> getMoveEventPlayers()
    {
        return moveEventPlayers;
    }
}
