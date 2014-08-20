package mereologic.net.pathfinderlootgen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Doug on 21/07/2014.
 */
public class Coins implements Treasure {

    public Coins as(Type thatType) {
        int thisOrdinal = this.type.ordinal();
        int thatOrdinal = thatType.ordinal();

        if (this.type.worthMoreThan(thatType)) {
            return new Coins(Type.values()[thisOrdinal - 1], quantity * 10).as(thatType);
        } else if (thatType.worthMoreThan(this.type)) {
            return new Coins(Type.values()[thisOrdinal + 1], quantity / 10).as(thatType);
        } else {
            return this;
        }
    }

    public Coins add(Coins other) {
//        if (this.type.worthMoreThan(other.getType())) {
//            return
//        }
//        Coins exchanged = this.type.worthMoreThan(other.type) ? other.type : this.type;
        Coins exchanged = other.as(this.type);
        return new Coins(type, exchanged.quantity + this.quantity);
    }

    public enum Type { COPPER, SILVER, GOLD, PLATINUM;
        public Coins coins(int quantity) {
            return new Coins(this, quantity);
        }
        public boolean worthMoreThan(Type other) { return this.ordinal() > other.ordinal(); }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private final Type type;
    private final int quantity;

    public Coins(Type type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public Treasure reroll() {
        return this; // fuck it
    }

    public Type getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public Coins getValue() {
        // coins are inherently valuable because metals are precious?
        return this;
    }

    @Override
    public String getName() {
        return "" + getQuantity() + " " + type.toString().toLowerCase();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(quantity);
        out.writeString(type.name());
    }

    public static final Parcelable.Creator<Coins> CREATOR = new Parcelable.Creator<Coins>() {

        @Override
        public Coins createFromParcel(Parcel parcel) {
            int quantity = parcel.readInt();
            Type type = Type.valueOf(parcel.readString());
            return new Coins(type, quantity);
        }

        @Override
        public Coins[] newArray(int size) {
            return new Coins[size];
        }
    };
}
