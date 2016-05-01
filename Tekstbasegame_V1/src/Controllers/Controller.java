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

    //TODO move input methods here
    public Controller() {
    }

    public void linkPlayer(Klasse player){
        this.player = player;
    }

    public void keyPressed(KeyEvent e) {
        int x = e.getKeyCode();
        if (x > 47 & x < 58) {
            player.setInput((char) x);
        } else if (x==KeyEvent.VK_I||x==KeyEvent.VK_R||x==KeyEvent.VK_E||x==KeyEvent.VK_S){
            player.setInput(e.getKeyChar());
        } else if(x == KeyEvent.VK_ENTER) {
            readInput();
        }
    }

    public void readInput(){
        if(player.getInputString().equals("i")){
            player.showInventory();
        }
        if(player.getInputString().equals("r")){
            //reset game
            player.resetGame();
        }
        if(player.getInputString().equals("e")){
            //exit game
            player.exitGame();
        }
        if(player.getInputString().equals("s")){
            player.showStats();
        }
        //Not a menu option
        if(player.getInput()<10&&player.getInput()>=0){
            player.setWaitForInput(false);
        }
    }


    public void keyTyped(KeyEvent e) {

    }
    public void keyReleased(KeyEvent e) {

    }
}
