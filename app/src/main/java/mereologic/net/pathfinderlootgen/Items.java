package mereologic.net.pathfinderlootgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.RandomAccess;

import static mereologic.net.pathfinderlootgen.Coins.Type.COPPER;
import static mereologic.net.pathfinderlootgen.Coins.Type.SILVER;

/**
 * Created by Doug on 25/07/2014.
 */
public class Items {

    private static final Item INVALID_ITEM = new Item("Stale Bread", COPPER.coins(1), Item.Type.MUNDANE);
    private static Map<String, Item> ITEMS_BY_NAME = new HashMap<String, Item>();
    private static Map<Item.Type, ArrayList<Item>> ITEMS_BY_TYPE = new HashMap<Item.Type, ArrayList<Item>>();

    static
    {
        for (Item.Type type : Item.Type.values()) ITEMS_BY_TYPE.put(type, new ArrayList<Item>());

        mundane("Boots", COPPER.coins(3));
        mundane("Hat", COPPER.coins(2));
        mundane("Gloves", SILVER.coins(1));
        mundane("Vest", COPPER.coins(5));
    }

    private static void mundane(String name, Coins value) {
        any(name, value, Item.Type.MUNDANE);
    }
    private static void minor(String name, Coins value) {
        any(name, value, Item.Type.MINOR_WONDROUS);
    }
    private static void any(String name, Coins value, Item.Type type) {
        Item item = new Item(name, value, Item.Type.MUNDANE);
        ITEMS_BY_NAME.put(name, item);
        ITEMS_BY_TYPE.get(type).add(item);
    }
    public static Item randomByType(Item.Type type) {
        return randomFrom(ITEMS_BY_TYPE.get(type));
    }

    private static <T> T randomFrom(ArrayList<T> items) {
        int randomIndex = (int) (Math.random() * items.size());
        return items.get(randomIndex);
    }

    public static Item findByName(String name) {
        return ITEMS_BY_NAME.containsKey(name) ? ITEMS_BY_NAME.get(name) : INVALID_ITEM;
    }
}
