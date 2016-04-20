package Items;

/**
 * Created by Sander on 13-3-2016.
 */
public class Item {
    private int damage;
    private boolean range;
    private int critChance;
    private String name;

    public Item(String name, int damage, boolean range, int critChance) {
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.critChance = critChance;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getCritChance() {
        return critChance;
    }
}
