package Controllers;

import Klassen.Klasse;
import sun.awt.ExtendedKeyCodes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Sander on 15-3-2016.
 */
public class Controller implements KeyListener {
    private Klasse player;
    public Controller(Klasse player) {
        this.player = player;
    }

    public void changePlayer(Klasse player){
        this.player = player;
    }

    public void keyPressed(KeyEvent e) {
        int x = e.getKeyCode();
        if (x > 47 & x < 58) {
            player.setInput(x-48);
        } else if(x == KeyEvent.VK_ENTER) {
            player.readInput();
        }
    }
    public void keyTyped(KeyEvent e) {

    }
    public void keyReleased(KeyEvent e) {

    }
}
