package com.hotmail.shinyclef.shinydynamics;

import org.bukkit.inventory.ItemStack;

/**
 * Author: ShinyClef
 * Date: 25/10/12
 * Time: 2:09 AM
 */

public class Books
{
    public static ItemStack danielleStart()
    {
        String title = "Danielle's Journal";
        String author = "Danielle Skylar";
        String[] pages = new String[3];

        pages[0] = "Grandpa was right, I knew it! This land does exist! " +
                "Then that must mean... Amathalya?" +
                "\nAll the stories he used to tell me! He wasn't crazy after all!" +
                "\nOk, get your head together Dani, think! Where would he be?";
        pages[1] = "He used to speak of a cabin. He said all his work was there. That must be where he went. " +
                "I must find him, too much depends on it, that much is certain." +
                "\nIf I'm going to have any chance of finding him, I'll have to think like him.";
        pages[2] = "So then, what would grandpa do? Well, he always used to say, " +
                "\"Where information is sought, information will be found.\"";

        BookItem bi = new BookItem(new ItemStack(387,1));
        bi.setTitle(title);
        bi.setAuthor(author);
        bi.setPages(pages);

        ItemStack writtenBook = bi.getItemStack();
        return writtenBook;
    }

    public static ItemStack georgeStudy()
    {
        String title = "Dear Dani";
        String author = "George Skylar";
        String[] pages = new String[3];

        pages[0] = "Dani, if you are reading this, then thank god you have come looking for me. " +
                "By now you have realized the stories I used to tell you were all true. Amathalya is real. " +
                "";
        pages[1] = "";
        pages[2] = "";

        BookItem bi = new BookItem(new ItemStack(387,1));
        bi.setTitle(title);
        bi.setAuthor(author);
        bi.setPages(pages);

        ItemStack writtenBook = bi.getItemStack();
        return writtenBook;
    }

    public static ItemStack amathalya()
    {
        String title = "Amathalya - Refuge in the Sky.";
        String author = "Unknown";
        String[] pages = new String[3];

        pages[0] = "Eons before our time when you and I would not yet recognize this world, " +
                "a spiritual people, known to us now as the Amal, called this land home. So spiritual were these " +
                "people that it is said they could commune with the essence of the land itself.";
        pages[1] = "The Amal were not the only descendants of those who came before. As kind and calm as " +
                "the Amal were, the Gryth were equally unsympathetic and ruthless in their unending search " +
                "for greater power.";
        pages[2] = "";

        BookItem bi = new BookItem(new ItemStack(387,1));
        bi.setTitle(title);
        bi.setAuthor(author);
        bi.setPages(pages);

        ItemStack writtenBook = bi.getItemStack();
        return writtenBook;
    }
}
