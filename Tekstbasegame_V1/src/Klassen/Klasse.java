package Klassen;

import Items.Item;
import Items.Kitchen_Knife;
import Main.TextBasedGame;
import Monsters.Dragon;
import Monsters.Goblin;
import Monsters.Monster;
import Monsters.Orc;
import Superclassess.AliveObj;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Sander on 13-3-2016.
 */
public class Klasse extends AliveObj{

    
    private static final int EXP_LEVEL_1 = 4000;

    private int hitpoints;
    private int maxHitpoints;
    private int day;
    private int damage;
    private int level;
    private int speed;

    private long exp = 0;
    private long expRequired;
    private boolean inFight;
    private Item equippedItem;

    private ArrayList<Monster> knownMonsters = new ArrayList<>();

    private ArrayList<Item> inventory = new ArrayList<>();

    private ArrayList<String> text = new ArrayList<>();

    private Random dice;
    private String name;

    private int input;
    private boolean waitForInput = true;
    private boolean interrupted = false;

    private TextBasedGame game;


    public Klasse() {
        this.exp = 0;
        this.expRequired = EXP_LEVEL_1;
        this.day = 1;
        this.level = 1;
        this.dice = new Random();
        this.name = "default name";
        this.knownMonsters.add(new Goblin());
        this.knownMonsters.add(new Dragon());
    }

    public void initClass(int hitpoints, int damage, int speed, Item startItem){
        this.hitpoints = hitpoints;
        this.maxHitpoints = hitpoints;
        this.damage = damage;
        this.equippedItem = startItem;
        this.speed = speed;
    }

    public void linkGame(TextBasedGame game){
        this.game = game;
    }

    public void chooseOption(int n){
        if(n == 1){
            sleep();
        } else {
            if (getKnownMonsters().size() >= n - 1) {
                track(getKnownMonsters().get(n - 2));
            }
        }
    }

    private void track(Monster m){
        addFastString("Tracking " + m + "s...");
        int i = dice.nextInt(100);
        //TODO make a track change for a monster dependant of the user level
        if(i<70){
            encounters(m);
        } else {
            if (i < 98) {
                encounters(discoverMonster());
            } else {
                encounters(new Dragon());
            }
        }
    }

    private Monster discoverMonster(){
        //TODO discover algorithm
        Monster m = new Orc();
        addString("You have discovered: " + m);
        getKnownMonsters().add(m);
        return m;
    }

    private void sleep(){
        addFastString("Getting some rest...");
        clearText();
        hitpoints = maxHitpoints;
        day++;
        addString("What a beautiful morning!");
    }

    public void showOptions(){
        addString("Type 1 to go to sleep");
        int i = 2;
        for(Monster m:getKnownMonsters()){
            addString("Type " + i + " to track " + m + "s");
            i++;
        }
    }

    public int getDamage(){
        int i = dice.nextInt(100);
        if (i<equippedItem.getCritChance()){
            addString("Crit!");
            return (int)(damage*1.5);
        }
        return damage;
    }

    public void encounters(Monster m){
        m.initMonster();
        addString("You've encounterd a level " + m.getLevel() + " " + m.getName());
        setInFight(true);
        while(inFight) {
            addString("The " + m + " has " + m.getHitpoints() + " hitpoints.");
            addString("What do you want to do?");
            addString("1. fight");
            addString("2. flee");
            waitForUserInput();
            int n = getInput();
            switch (n) {
                case 1:
                    fight(m);
                    break;
                case 2:
                    if(flee(m)) {
                        //flee was succesful
                        inFight = false;
                    }
                    break;
            }
        }
    }

    private void checkLevelUp(){
        while(exp >= expRequired){
            this.level++;
            addString("Congratulations! You've gained a level!");
            addString("You are now level: " + this.level);
            expRequired = (expRequired*(190+dice.nextInt(100)+1))/100;
        }
    }

    private void increaseExp(int e){
        this.exp += e;
        checkLevelUp();
    }

    private void fight(Monster m){
        if(m.getSpeed() < this.getSpeed()){
            hit(this,m);
        } else{
            hit(m,this);
        }
    }

    private boolean processDeath(AliveObj a){
        if(!a.isAlive()){
            if (a instanceof Monster) {
                addString("You defeated the " + a + "!");
                Monster m = (Monster) a;
                increaseExp(m.getExp());
                setInFight(false);
                changed();
            } else {
                gameover();
            }
            return true;
        }
        return false;
    }

    private void hit(AliveObj first,AliveObj second){
        second.subtractHitpoints(first.getDamage());
        if(!processDeath(second)) {
            first.subtractHitpoints(second.getDamage());
            processDeath(first);
        }
    }

    //returns if the flee was successful and if not deal damage to the player
    private boolean flee(Monster m){
        int x = dice.nextInt(100);
        if (x < m.getFlee()){
            addString("Success!");
            return true;
        } else {
            addString("Fail!");
            this.subtractHitpoints(m.getDamage());
            processDeath(this);
            return false;
        }
    }

    public void readInput(){
        if(getInput() == 8){
            String s="You have a ";
            for(Item i:getInventory()){
                s += i + ", ";
            }
            if(equippedItem!=null){
                addString("Equipped item: "+ equippedItem);
            }
            if (getInventory().isEmpty()) {
                addFastString("Your inventory is empty");
            } else {
                addFastString(s);
            }
        }
        if(getInput() == 9){
            game.resetGame();
        }
        if(getInput() == 0){
            System.exit(0);
        }
        //Not a menu option
        if(getInput()<8){
            setWaitForInput(false);
        }
    }

    public int waitForUserInput(){
        setWaitForInput(true);
        while(isWaitForInput()||interrupted){
            setWaitForInput(true);
            try{
                Thread.sleep(500);
            } catch(Exception e){
                e.printStackTrace();
            }

        }
        return getInput();
    }


    //Let the thread wait until it gets user input
    public int waitForUserInput(int upperbound){
        setWaitForInput(true);
        while(isWaitForInput()||(getInput()>upperbound)||interrupted){
            setWaitForInput(true);
            try{
                Thread.sleep(500);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return getInput();
    }

    private void gameover(){
        addString("You did not achieve to kill the dragon sucker go get yourself a drink and play again");
        waitForUserInput();
        game.resetGame();
    }


    public void setInput(int key){
        this.input = key;
        changed();
    }

    public ArrayList<Monster> getKnownMonsters() {
        ArrayList<Monster> copy = knownMonsters;
        return copy;
    }

    public ArrayList<Item> getInventory() {
        ArrayList<Item> copy = inventory;
        return copy;
    }

    public void setInFight(boolean inFight) {
        this.inFight = inFight;
    }

    public int getInput() {
        return input;
    }

    public boolean isWaitForInput() {
        return waitForInput;
    }

    public void setWaitForInput(boolean waitForInput) {
        this.waitForInput = waitForInput;
    }

    public int getLevel() {
        return level;
    }

    public long getExp() {
        return exp;
    }

    public long getExpRequired() {
        return expRequired;
    }

    public int getDay() {
        return day;
    }

    public int getMaxHitpoints() {
        return maxHitpoints;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getSpeed() {
        return speed;
    }

    public void subtractHitpoints(int x){
        this.hitpoints -= x;
    }

    public boolean isAlive(){
        return hitpoints > 0;
    }

    public String toString(){
        return name;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public void addFastString(String s){
        if(!interrupted) {
            if (getText().size() > 25) {
                text.remove(0);
            }
            text.add(s);
            changed();
        }
    }

    public void addString(String s){
        addFastString(s);
    }

    public ArrayList<String> getText(){
        ArrayList<String> copy = text;
        return copy;
    }

    public void clearText(){
        this.text.clear();
    }

    public void changed(){
        this.setChanged();
        this.notifyObservers();
    }

}
