package com.hotmail.shinyclef.shinydynamics;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Author: ShinyClef
 * Date: 24/10/12
 * Time: 3:15 AM
 */

public class CmdExecutor implements CommandExecutor
{
    private ShinyDynamics plugin;
    private Set<String> MoveEventPlayers;
    private World world;
    private World atlantis;

    public CmdExecutor(ShinyDynamics plugin, Set<String> MoveEventPlayers)
    {
        this.plugin = plugin;
        this.MoveEventPlayers = MoveEventPlayers;
        world = plugin.getServer().getWorld("world");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("rolyd.mod"))
        {
            sender.sendMessage(ChatColor.RED + "Sorry, you don't have permission to do that.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("hoi"))
        {
            if (args[0].equalsIgnoreCase("addroom"))
            {
                if (args.length != 3)
                {
                    sender.sendMessage(ChatColor.RED + "/hoi addroom <keyword> <filename>");
                    return true;
                }

                if (!args[1].toLowerCase().matches("[a-z.-]+"))
                {
                    sender.sendMessage(ChatColor.RED + "The keyword must contain only letters, hyphens or full stops.");
                    return true;
                }

                Player player = (Player) sender;
                Location loc = player.getLocation();
                if (!loc.getWorld().getName().equals("world"))
                {
                    player.sendMessage(ChatColor.RED + "You must be in main world. You should perform this command" +
                            "in the house of illusions room at the exact location you did the //copy from.");
                    return true;
                }

                HouseOfIllusions.addScene(args[1].toLowerCase(), args[2].toLowerCase(), loc);
                sender.sendMessage(ChatColor.DARK_GREEN + "Room " + ChatColor.GOLD + args[1].toLowerCase() +
                        ChatColor.DARK_GREEN + " has been added with filename " +
                        ChatColor.GOLD + args[2].toLowerCase() + ChatColor.DARK_GREEN + ".");
                return true;
            }
        }

        if (command.getName().equalsIgnoreCase("test1"))
        {
            HouseOfIllusions.closeEntrance();
            return HouseOfIllusions.closeRoom();
        }

        if (command.getName().equalsIgnoreCase("test2"))
        {
            HouseOfIllusions.openEntrance();
            return HouseOfIllusions.openRoom();
        }

        if (command.getName().equalsIgnoreCase("test3"))
        {
            HouseOfIllusions.deactivateBeacon();
            return true;
        }

        if (command.getName().equalsIgnoreCase("test4"))
        {
            HouseOfIllusions.clearScene();
            return true;
        }

        if (command.getName().equalsIgnoreCase("sound"))
        {
            if (args.length < 2)
            {
                return false;
            }

            Location loc = Bukkit.getServer().getPlayer(sender.getName()).getLocation();
            world.playSound(loc, Sound.valueOf(args[0].toUpperCase()),
                    Float.parseFloat(args[1]), Float.parseFloat(args[2]));
            return true;
        }

        if (command.getName().equalsIgnoreCase("copy"))
        {
            return copy();
        }

        return false;
    }



    private boolean copy()
    {
        QuestResources.deployChest(0, LocationManager.getDanielleStartLoc());
        return true;
    }

}
