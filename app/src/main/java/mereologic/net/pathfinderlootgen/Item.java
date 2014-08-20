package mereologic.net.pathfinderlootgen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Doug on 21/07/2014.
 */
public class Item implements Treasure {

    public enum Type {
        MUNDANE, MINOR_WONDROUS, MAGICAL
    }

    private final String name;
    private final Coins value;
    private Type type;

    public Item(String name, Coins value, Type type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    @Override
    public Coins getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Treasure reroll() {
        return Items.randomByType(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return Items.findByName(in.readString());
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
