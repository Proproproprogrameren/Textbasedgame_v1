import Controllers.Controller;
import Klassen.Klasse;
import Klassen.Mage;
import Klassen.Rogue;
import Klassen.Warrior;
import Monsters.Goblin;
import Monsters.Monster;
import View.View;

import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Sander on 13-3-2016.
 */
public class Textbasedgame extends JFrame implements Runnable {
    Klasse player;
    Random dice;
    View view;
    Controller controller;
    private String randomName;

    public Textbasedgame() {
        super("Game!"); // Add title.
        player = new Klasse();
        view = new View(player);
        controller = new Controller(player);  // Reading from System.in
        this.addKeyListener(controller);
        dice = new Random();
        randomName = genRandomName();

        add(view);

        setDefaultCloseOperation(EXIT_ON_CLOSE); //What happens when the frame closes

        setSize(view.getWidth() + 32, view.getHeight() + 32); //Set frame size
        setVisible(true); //make it visible
    }
    private String genRandomName(){
        String s = "";
        int len = dice.nextInt(5)+5;
        for(int i = 1; i < len; i++){
            s += (char) (97+dice.nextInt(26));
        }
        return s;
    }

    public String getRandomName() {
        return randomName;
    }

    public void run(){
        view.addString("You're a poor farmer in the kingdom of " + getRandomName());
        view.addString("A dragon has been terrorizing " + getRandomName() + " for a while now.");
        view.addString("So you, as the hero that you are, have decided to put an end this drama!");
        view.addString("You’ve given up the noble profession of farming to become a:");
        view.addString("Type 1 for warrior");
        view.addString("Type 2 for mage");
        view.addString("Type 3 for rogue");

        player.waitForUserInput();
        int n = player.getInput();
        String s = "";

        switch(n){
            case 1:
                view.addFastString("You've chosen the warrior class!");
                this.player = new Warrior(view);
                s = "warrior";
                break;
            case 2:
                view.addFastString("You've chosen the mage class!");
                this.player = new Mage(view);
                s = "mage";
                break;
            case 3:
                view.addFastString("You've chosen the rogue class!");
                this.player = new Rogue(view);
                s = "rogue";
                break;
        }
        controller.changePlayer(player);
        view.changePlayer(player);

        view.addString("You know that as a level one " + s + " you won’t be able to face this dragon for sure,");
        view.addString("so before challenging the dragon you must train.");
        view.addString("You know there are goblin’s roaming the land and perhaps other creatures.");
        view.addString("To fill your days you can track creatures or sleep to regain strength.");
        view.addString("Don’t waste too much time because the dragon will just keep on terrorizing!");
        view.addString("What do you want to do?");
        while(!player.isStory1Complete()) {
            player.showOptions();
            player.waitForUserInput();
            n = player.getInput();
            player.chooseOption(n);
        }



        player.encounters(new Goblin());
        player.encounters(new Goblin());
        player.encounters(new Goblin());
        player.encounters(new Goblin());
        player.encounters(new Goblin());

    }

}
