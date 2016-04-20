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
    private ArrayList<String> text;
    private Klasse player;
    private String[] menu = {
        "9. Exit","0. Items"
    };
    public View(Klasse player) {
        this.player = player;
        player.linkView(this);
        this.player.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                View.this.repaint();
            }
        });
        setSize(800,500); //Set panel size
        setVisible(true);
        text = new ArrayList<>();
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
        for(String s : getText()){
            g.drawString(s,10,10+15*i);
            i++;
        }
        g.drawString(Integer.toString(player.getInput()),10,450);

    }
    public void addFastString(String s){
        if(getText().size()>25) {
            text.remove(0);
        }
        text.add(s);
        this.repaint();

    }
    public void addString(String s){
        try{
            Thread.sleep(500);
        } catch (Exception e){
            e.printStackTrace();
        }
        addFastString(s);
    }

    private ArrayList<String> getText(){
        ArrayList<String> copy = text;
        return copy;
    }

    public void clearText(){
        this.text.clear();
    }

    public String[] getMenu() {
        return menu;
    }

    public void changePlayer(Klasse player){
        this.player = player;
        this.player.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                View.this.repaint();
            }
        });
    }
}
