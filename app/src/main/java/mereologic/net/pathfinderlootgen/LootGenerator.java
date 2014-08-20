package mereologic.net.pathfinderlootgen;

import java.util.ArrayList;
import java.util.List;

import static mereologic.net.pathfinderlootgen.Coins.Type.COPPER;
import static mereologic.net.pathfinderlootgen.Coins.Type.GOLD;

/**
 * Created by Doug on 21/07/2014.
 */
public class LootGenerator {

    public enum Progression { SLOW, MEDIUM, FAST }

    private static final int[][] GOLD_PER_LEVEL_AND_PROGRESSION = new int [][] {
        {170, 260, 400},
        {350, 550, 800}
    };

    public static ArrayList<Treasure> generate(int level, Progression progression) {
        int goldForEncounter = GOLD_PER_LEVEL_AND_PROGRESSION[level - 1][progression.ordinal()];
        int copperRemaining = new Coins(GOLD, goldForEncounter).as(COPPER).getQuantity();

        ArrayList<Treasure> items = new ArrayList<Treasure>();
        while(copperRemaining > 0 && items.size() < 50) {
            Item item = Items.randomByType(Item.Type.MUNDANE);
            copperRemaining -= item.getValue().as(COPPER).getQuantity();
            items.add(item);
        }

        return items;
    }
}
