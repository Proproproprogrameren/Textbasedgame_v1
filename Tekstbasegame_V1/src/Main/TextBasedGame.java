package Main;


import Items.Item;
import Items.Kitchen_Knife;
import Items.Twig;
import Items.Wooden_Sword;
import Klassen.Klasse;

import NameCreate.NameGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


/**
 * Created by Sander on 13-3-2016.
 */
public class TextBasedGame implements Runnable {

    private static final int ROGUE_HP = 12;
    private static final int ROGUE_DAMAGE = 3;
    private static final int ROGUE_SPEED = 20;
    private static final Item ROGUE_START_ITEM = new Kitchen_Knife();

    private static final int MAGE_HP = 12;
    private static final int MAGE_DAMAGE = 3;
    private static final int MAGE_SPEED = 20;
    private static final Item MAGE_START_ITEM = new Twig();

    private static final int WARRIOR_HP = 12;
    private static final int WARRIOR_DAMAGE = 3;
    private static final int WARRIOR_SPEED = 20;
    private static final Item WARRIOR_START_ITEM = new Wooden_Sword();





    private Random dice;
    private String randomName;
    private Klasse player;
    private TextbasedFrame frame;
    private boolean running;
    private ArrayList<String> story; //Can be made a queue for efficientcy
    private int lineNumber;

    private boolean waitForInput;
    private boolean interrupted;

    private boolean inStoryMode;
    private boolean inStoryOne;

    private int input;

    private String klasse;

    public TextBasedGame(Klasse player, TextbasedFrame frame) {
        this.player = player;

        player.linkGame(this);

        this.frame = frame;
        this.lineNumber = 0;

        this.inStoryMode = true;
        this.inStoryOne = false;

        this.waitForInput = false;
        this.interrupted = false;

        NameGenerator gen = new NameGenerator();
        randomName = gen.getName();
        klasse = "";

        dice = new Random();

        readStory();

        this.running = true;

    }

    private void printStoryline(){
        if (lineNumber<getStory().size()) {
            if (lineNumber==0){
                player.addFastString(checkGets(getStory().get(lineNumber)));
            } else {
                player.addString(checkGets(getStory().get(lineNumber)));
            }
            lineNumber++;
            //Choose class after 7 lines
            if(lineNumber==7){
                this.waitForInput = true;
                chooseClass();
            }
            if(lineNumber==12) {
                inStoryMode=false;
                inStoryOne=true;
            }
        } else {
            System.out.println("out of story :(");
        }
    }

    private void dayTimeActivity(){
        setWaitForInput(true);
        input = waitForUserInput(player.showOptions());
        player.chooseOption(input);
        setWaitForInput(false);
        wait(500);
    }

    private void updateGame(){
        if(!isWaitForInput()) {
            assert isInStoryMode() == isInStoryOne();
            if (isInStoryMode()) {
                printStoryline();
            }

            if(isInStoryOne()) {
                player.addFastString("What do you want to do?");
                dayTimeActivity();
            }
        } else {
            wait(100);
        }
    }

    public void run() {
        while (isRunning()) {
            updateGame();
        }
        System.out.println("savely ended thread");
    }

    private void chooseClass(){
        input = waitForUserInput(3);
        if (input==1||input==2||input==3) {
            if (input == 1) {
                player.initClass(WARRIOR_HP, WARRIOR_DAMAGE, WARRIOR_SPEED, WARRIOR_START_ITEM,1);
                klasse = "warrior";
            }
            if (input == 2) {
                player.initClass(MAGE_HP, MAGE_DAMAGE, MAGE_SPEED, MAGE_START_ITEM,2);
                klasse = "mage";
            }
            if (input == 3) {
                player.initClass(ROGUE_HP, ROGUE_DAMAGE, ROGUE_SPEED, ROGUE_START_ITEM,3);
                klasse = "rogue";
            }
            setWaitForInput(false);
        }
    }

    private String checkGets(String s){
        if (s.contains("getRandomName")){
            s  = s.replaceAll("getRandomName", getRandomName());
        }
        if(s.contains("getKlasse")){
            s  = s.replaceAll("getKlasse", getKlasse());
        }
        return s;
    }


    private void readStory(){
        URL f = TextBasedGame.class.getClassLoader().getResource("resources/story.txt");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(f.openStream()))) {
            story = new ArrayList<>();
            String line = br.readLine();
            while (line != null) {
                story.add(line);
                line = br.readLine();
            }
        } catch (Exception e){
            System.out.println("story file not found");
            e.printStackTrace();
        }
    }

    public String getKlasse(){
        return klasse;
    }

    public void resetGame(){
        this.interrupted = true;
        this.running = false;
        frame.resetGame();
    }

    public boolean isInStoryMode() {
        return inStoryMode;
    }

    public boolean isInStoryOne(){
        return inStoryOne;
    }

    public void wait(int secs){
        try{
            Thread.sleep(secs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning(){
        return running;
    }

    private String getRandomName() {
        return randomName;
    }

    public ArrayList<String> getStory() {
        return story;
    }

    public boolean isWaitForInput() {
        return waitForInput;
    }

    public void setWaitForInput(boolean waitForInput) {
        this.waitForInput = waitForInput;
    }

    public boolean isInterrupted() {
        return interrupted;
    }


    //any input is okay
    public  int waitForUserInput(){
        return waitForUserInput(10);
    }

    //Let the thread wait until it gets user input
    public int waitForUserInput(int upperbound){
        player.setWaitForInput(true);
        while(player.isWaitForInput()||player.getInput()>upperbound) {
            //setWaitForInput(true);
            player.setWaitForInput(true);
            if (interrupted) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return player.getInput();
    }
}
