package com.hotmail.shinyclef.shinydynamics;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

/**
 * Author: ShinyClef
 * Date: 24/10/12
 * Time: 9:24 PM
 */

public class QuestResources
{
    private static ShinyDynamics plugin = ShinyDynamics.getPlugin();
    private static World world = plugin.getServer().getWorld("world");
    private static Chest[] chests = new Chest[7];
    private static Inventory[] invs = new Inventory[7];

    /* Get Chest and Inventory objects for each chest in resources room */
    public static void mapChests()
    {
        int x = 3234;
        int y = 1;
        int z = 1789;

        for (int i = 0; i < 7; i++)
        {
            chests[i] = (Chest)world.getBlockAt(x, y, z).getState();
            invs[i] = chests[i].getBlockInventory();
            x = x + 2;
        }
    }

    private static void clearResourceChests()
    {
        for (Inventory inv : invs)
        {
            inv.clear();
        }
    }

    /* Puts the quest items into the chests */
    public static void populateChests()
    {
        chest0();
        chest1();
        chest2();
        chest3();
        chest4();
        chest5();
        chest6();
    }

    private static void chest0()
    {
        invs[0].addItem(Books.danielleStart());
    }

    private static void chest1()
    {

    }

    private static void chest2()
    {

    }

    private static void chest3()
    {

    }

    private static void chest4()
    {

    }

    private static void chest5()
    {

    }

    private static void chest6()
    {

    }

    /* Copies a chest from resources to a location in the world.
     * Params: index: The index of the inventory from resources to copy.
     *         loc: the location to copy to. */
    public static void deployChest(int index, Location loc)
    {
        //create a chest at target location
        world.getBlockAt(loc).setTypeId(54);

        //reference the new chest and its inventory
        Chest chest = (Chest) world.getBlockAt(loc).getState();
        Inventory newInv = chest.getBlockInventory();

        //clone contents of resources inventory into new chest inventory
        newInv.setContents(invs[index].getContents().clone());
    }
}
