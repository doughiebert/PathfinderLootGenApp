package mereologic.net.pathfinderlootgen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Doug on 25/07/2014.
 */
public interface Treasure extends Parcelable {
    Coins getValue();
    String getName();
    Treasure reroll();
}
