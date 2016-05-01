package View;

import Klassen.Klasse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sander on 14-3-2016.
 */
public class View extends JPanel {
    // All the text on the screen
    private ArrayList<String> text;
    // The model
    private Klasse player;
    // menu options
    private String[] menu = {
            "S. Stats",
            "I. Items",
        "R. Restart",
            "E. Exit"
    };

    public View(int width, int height) {
        setSize(width,height); //Set panel size
        setVisible(true);
    }

    public void linkPlayer(Klasse player){
        this.player = player;
        this.player.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                View.this.repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws Background
        this.setBackground(new Color(255,255,255));

        // Draws Menu
        int i = 0;
        g.setColor(new Color(0, 0, 0));
        g.drawLine(550,0,550,500);
        for(String s : getMenu()){
            g.drawString(s,560,100+15*i);
            i++;
        }

        // Draws Level/Exp/Exp required
        g.drawString("Day: " + player.getDay(),560,10);
        g.drawString("Level: " + player.getLevel(),560,25);
        g.drawString("Experience: " + player.getExp(), 560, 40);
        g.drawString("Next Level: " + player.getExpRequired(), 560, 55);
        g.drawString("Hitpoints: " + player.getHitpoints() + "/" + player.getMaxHitpoints(),560,70);

        // Draws text
        i = 0;
        for(String s : player.getText()){
            g.drawString(s,10,10+15*i);
            i++;
        }

        //draw inputline
        g.drawString(player.getInputString(),10,450);

    }

    private String[] getMenu() {
        return menu;
    }

}
