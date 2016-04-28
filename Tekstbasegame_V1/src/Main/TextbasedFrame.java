package Main;

import Controllers.Controller;
import Klassen.Klasse;
import View.View;

import javax.swing.*;

/**
 * Created by twan on 23-4-2016.
 */
public class TextbasedFrame extends JFrame{

    private View view;
    private Klasse player;
    private Controller controller;
    private TextBasedGame game;
    private static final int WIDTH = 800;
    private  static final int HEIGHT = 500;


    public TextbasedFrame() {
        super("Tekstbasedgame!"); //set title

        this.view = new View(WIDTH, HEIGHT); // init view
        this.add(view);

        this.controller = new Controller(); //init controller
        this.addKeyListener(controller);

        initPlayer(); //init player and link it to view and controller

        setSize(WIDTH+32,HEIGHT+32); //Set frame size
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE); //What happens when the frame closes

    }

    private void initPlayer(){
        this.player = new Klasse();
        view.linkPlayer(player);
        controller.linkPlayer(player);
        this.game = new TextBasedGame(player, this);
        new Thread(game).start();
    }

    public void resetGame(){
        System.out.println("Start new thread");
        initPlayer();
    }
}
