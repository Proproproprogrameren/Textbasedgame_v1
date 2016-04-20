package Klassen;

import Items.Kitchen_Knife;
import View.View;

/**
 * Created by Sander on 13-3-2016.
 */
public class Rogue extends Klasse {
    public Rogue(View view) {
        super(12, 2, new Kitchen_Knife(), 20, view);
    }
}
