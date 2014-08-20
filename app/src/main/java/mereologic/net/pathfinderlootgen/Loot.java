package mereologic.net.pathfinderlootgen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Doug on 21/07/2014.
 */
public class Loot {
    private final Item item;
    private final int quantity;

    public Loot(int quantity, Item item) {
        this.quantity = quantity;
        this.item = item;
    }
}
