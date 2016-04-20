package Superclassess;

import java.util.Observable;

/**
 * Created by Sander on 15-3-2016.
 */
public abstract class  AliveObj extends Observable {
    public abstract boolean isAlive();
    public abstract int getDamage();
    public abstract void subtractHitpoints(int x);
    public abstract String toString();
}
