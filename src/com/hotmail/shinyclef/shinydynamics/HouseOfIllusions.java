package com.hotmail.shinyclef.shinydynamics;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Author: ShinyClef
 * Date: 6/11/12
 * Time: 12:35 AM
 */
public abstract class HouseOfIllusions
{
    private static ShinyDynamics plugin = ShinyDynamics.getPlugin();
    private static World world = Bukkit.getWorld("world");
    private static Map<Character, Block> leverBlockMap;
    private static Map<String, Scene> sceneMap;
    private static List<String> sceneList;
    private static List<String> hoiMoveEvents;
    private static boolean roomIsClosed;

    public static void setup()
    {
        setupLeverMap();
        setupSceneMap();
        hoiMoveEvents = plugin.getConfig().getStringList("HouseOfIllusions.InHouse");
        roomIsClosed = false;
    }

    private static void setupLeverMap()
    {
        leverBlockMap = new HashMap<Character, Block>();
        Block block;
        Sign sign;
        char letter;
        int x = 167;
        int y = 60;
        int z = -130;

        while (y < 62)
        {
            while (x < 174)
            {
                while (z < -119)
                {
                    block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.LEVER)
                    {
                        sign = (Sign) world.getBlockAt(x, 62, z). getState();
                        letter = sign.getLine(y == 60 ? 2 : 1).toLowerCase().charAt(2);
                        leverBlockMap.put(letter, block);
                    }
                    z++;
                }
                x++;
                z = -130;
            }
            y++;
            x = 167;
        }
    }

    private static void setupSceneMap()
    {
        //format of a line: keyword,testFileName,x,y,z

        sceneMap = new HashMap<String, Scene>();
        sceneList = plugin.getConfig().getStringList("HouseOfIllusions.Scenes");

        for (String line : sceneList)
        {
            String[] part = line.split(" ");
            Scene scene = new Scene(part[0], toSceneCode(part[0]), part[1],
                    Integer.parseInt(part[2]), Integer.parseInt(part[3]), Integer.parseInt(part[4]));

            sceneMap.put(scene.sceneCode, scene);
        }
    }

    public static boolean openRoom()
    {
        world.getBlockAt(174, 23, -126).setTypeId(0); //air
        world.getBlockAt(175, 15, -124).setTypeId(0);
        world.getBlockAt(175, 15, -125).setTypeId(0);
        world.getBlockAt(175, 15, -126).setTypeId(0);
        return true;
    }

    public static boolean closeRoom()
    {
        world.getBlockAt(174, 23, -126).setTypeId(76); //redstone torch
        world.getBlockAt(175, 15, -124).setTypeId(76);
        world.getBlockAt(175, 15, -125).setTypeId(76);
        world.getBlockAt(175, 15, -126).setTypeId(76);
        return true;
    }

    public static boolean openEntrance()
    {
        for (int z = -126 ; z < -123 ; z++)
        {
            for (int y = 61 ; y < 65 ; y++)
            {
                world.getBlockAt(158, y, z).setTypeId(0); //air
                world.playEffect(new Location(world, 158, y, z), Effect.SMOKE, BlockFace.UP);
            }
        }

        world.playSound(new org.bukkit.Location(world, 158D, 61D, -126D), Sound.BLAZE_DEATH, 1, 0);

        return true;
    }

    public static boolean closeEntrance()
    {
        for (int z = -126 ; z < -123 ; z++)
        {
            for (int y = 61 ; y < 65 ; y++)
            {
                world.getBlockAt(158, y, z).setTypeId(4); //cobblestone
            }
        }
        return true;
    }

    public static boolean activateBeacon()
    {
        world.getBlockAt(170, 3, -125).setTypeId(20); //glass
        return true;
    }

    public static boolean deactivateBeacon()
    {
        world.getBlockAt(170, 3, -125).setTypeIdAndData(35, DyeColor.BLACK.getData(), true);
        return true;
    }

    public static void buttonPress(Player player)
    {
        Bukkit.broadcastMessage(sceneMap.keySet() + "");
        String activeCode = getLeverCode();
        if (sceneMap.keySet().contains(activeCode))
        {
            matchingCode(player, activeCode);
        }
        else
        {
            incorrectCode();
        }
    }

    private static String getLeverCode()
    {
        String code = "";
        for (Map.Entry<Character, Block> entry : leverBlockMap.entrySet())
        {
            Lever lever = new Lever(entry.getValue().getData(), entry.getValue().getData());
            if (lever.isPowered())
            {
                code = code + String.valueOf(entry.getKey());
            }
        }
        code = sortAlphabetically(code);
        return code;
    }

    public static void resetLevers()
    {
        for (Block block : leverBlockMap.values())
        {
            Lever lever = new Lever(block.getData(), block.getData());
            lever.setPowered(false);
            block.setData(lever.getData());
        }
    }

    private static void matchingCode(Player player, String code)
    {
        //play a sound
        world.playSound(new Location(world, 170, 57, -125), Sound.AMBIENCE_CAVE, 1, 0);

        //shut room, open passage
        world.getBlockAt(170, 58, -125).breakNaturally();
        closeRoom();

        //register player for move events
        registerMoveEvents(player.getName());

        //light the beacon!
        activateBeacon();

        //load schematic
        String schematicFileName = sceneMap.get(code).filename;
        Vector vector = new Vector(sceneMap.get(code).x, sceneMap.get(code).y, sceneMap.get(code).z);
        loadSchematic(player, schematicFileName, vector);
    }

    private static void incorrectCode()
    {
        //play a sound
        resetLevers();
    }

    private static boolean loadSchematic(CommandSender sender, String schematicFileName, Vector vector)
    {
        Plugin we = Bukkit.getPluginManager().getPlugin("WorldEdit");

        //get the file we want to load from W/E folder
        File schematics = new File(we.getDataFolder(), "schematics");
        File file = new File(schematics, schematicFileName + ".schematic");

        if (!file.exists())
        {
            sender.sendMessage("File doesn't exist.");
            return true;
        }

        //get a LocalWorld from the bukkit world
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        LocalWorld localWorld = bukkitWorld;

        //create the edit session using the world
        EditSession editSession = new EditSession(localWorld, 10000);

        //create a new schematic format to use
        SchematicFormat schematicFormat = SchematicFormat.getFormat("MCE");

        //use this schematic format to load the schematic into a cuboid clipboard
        CuboidClipboard cuboidClipboard =  null;
        try
        {
            cuboidClipboard = schematicFormat.load(file);
        }
        catch (IOException ex)
        {
            sender.sendMessage(ex.getMessage());
        }
        catch (DataException ex)
        {
            sender.sendMessage(ex.getMessage());
        }

        //create a WE vector to place things at
        //Vector vector = new Vector(165, 64, -150);

        //paste from clipboard
        try
        {
            cuboidClipboard.paste(editSession, vector, true);
        }
        catch (MaxChangedBlocksException ex)
        {
            sender.sendMessage(ex.getMessage());
        }
        return true;
    }

    public static void clearScene()
    {
        Block block;
        int x = 177;
        int y = 0;
        int z = -150;

        while (y < 61)
        {
            while (x < 228)
            {
                while (z < -99)
                {
                    block = world.getBlockAt(x, y, z);
                    if (block.getTypeId() != 0)
                    {
                        block.setTypeId(0);
                    }
                    z++;
                }
                x++;
                z = -150;
            }
            y++;
            x = 177;
        }
    }

    private static String toSceneCode(String keyword)
    {
        String sorted = sortAlphabetically(keyword);
        return removeDuplicatesFromSortedString(sorted);
    }

    private static String sortAlphabetically(String keyword)
    {
        char[] chars = keyword.toCharArray();
        java.util.Arrays.sort(chars);
        return new String(chars);
    }

    private static String removeDuplicatesFromSortedString(String input)
    {
        StringBuilder noDupes = new StringBuilder();
        noDupes.append(input.charAt(0));
        for (int i = 0; i < input.length() - 1; i++)
        {
            char c1 = input.charAt(i);
            char c2 = input.charAt(i + 1);

            if (c1 != c2)
            {
                noDupes.append(c2);
            }
        }
        return noDupes.toString();
    }

    public static void addScene(String keyword, String schematicFileName, Location loc)
    {
        int x, y, z;
        x = (int) loc.getX();
        y = (int) loc.getY();
        z = (int) loc.getZ();

        //add to the scene list
        sceneList.add(keyword + " " + schematicFileName + " " + x + " " + y + " " + z);

        //put a new scene object into the scene map
        Scene scene = new Scene(keyword, toSceneCode(keyword), schematicFileName, x, y, z);
        sceneMap.put(scene.sceneCode, scene);

        //save to config
        plugin.getConfig().set("HouseOfIllusions.Scenes", sceneList);
        plugin.saveConfig();
    }

    private static void registerMoveEvents(String playerName)
    {
        if(!hoiMoveEvents.contains(playerName))
        {
            EventListener.getMoveEventPlayers().add(playerName);
            hoiMoveEvents.add(playerName);
            plugin.getConfig().set("HouseOfIllusions.InHouse", hoiMoveEvents);
            plugin.saveConfig();

        }
    }

    private static void unregisterMoveEvents(String playerName)
    {
        EventListener.getMoveEventPlayers().remove(playerName);
        hoiMoveEvents.remove(playerName);
        plugin.getConfig().set("HouseOfIllusions.InHouse", hoiMoveEvents);
        plugin.saveConfig();
        Bukkit.getPlayer(playerName).sendMessage("Unregistering " + playerName + ". Still registered: " + hoiMoveEvents);
    }

    public static void moveEvent(Player player)
    {
        if (!hoiMoveEvents.contains(player.getName()))
        {
            return;
        }

        Location loc = player.getLocation();
        double x = loc.getX();
        double z = loc.getZ();

        //first check if they are out of the x,z of HoI
        if (x > 228 || x < 153 || z < -151 || z > -99)
        {
            unregisterMoveEvents(player.getName());

            //if there are no other players in the room
            if (hoiMoveEvents.isEmpty())
            {
                deactivateBeacon();
                closeRoom();
                closeEntrance();
                clearScene();
                resetLevers();
                return;
            }
        }

        double y = loc.getY();
        if (y < 19 && x > 171)
        {
            openRoom();
        }
    }

    private static class Scene
    {
        String keyword;
        String sceneCode;
        String filename;
        int x, y, z;

        private Scene(String keyword, String sceneCode, String filename, int x, int y, int z)
        {
            this.keyword = keyword;
            this.sceneCode = sceneCode;
            this.filename = filename;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}