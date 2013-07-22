package com.hotmail.shinyclef.shinydynamics;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: ShinyClef
 * Date: 24/10/12
 * Time: 3:10 AM
 */

public class ShinyDynamics extends JavaPlugin
{
    private static ShinyDynamics plugin;

    private CmdExecutor commandExecutor;
    private Set<String> moveEventPlayers;
    private World world;
    private World atlantis;

    @Override
    public void onEnable()
    {
        plugin = this;
        saveDefaultConfig();

        //set some stuff up
        initialize();

        //bukkit formalities
        new EventListener(this, moveEventPlayers);
        commandExecutor = new CmdExecutor(this, moveEventPlayers);
        getCommand("hoi").setExecutor(commandExecutor);
        getCommand("test1").setExecutor(commandExecutor);
        getCommand("test2").setExecutor(commandExecutor);
        getCommand("test3").setExecutor(commandExecutor);
        getCommand("test4").setExecutor(commandExecutor);
        getCommand("sound").setExecutor(commandExecutor);
        getCommand("copy").setExecutor(commandExecutor);
    }

    @Override
    public void onDisable()
    {

    }

    private void initialize()
    {
        //moveEventPlayers.add("shinyclef");
        moveEventPlayers = new HashSet<String>();
        world = getServer().getWorld("world");
        QuestResources.mapChests();
        HouseOfIllusions.setup();
    }

    public static ShinyDynamics getPlugin()
    {
        return plugin;
    }
}
