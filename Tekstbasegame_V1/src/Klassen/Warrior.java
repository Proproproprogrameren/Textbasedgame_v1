package Klassen;

import Items.Wooden_Sword;
import View.View;

/**
 * Created by Sander on 13-3-2016.
 */
public class Warrior extends Klasse {
    public Warrior(View view) {
        super(150000, 500000, new Wooden_Sword(), 5, view);
    }
}
