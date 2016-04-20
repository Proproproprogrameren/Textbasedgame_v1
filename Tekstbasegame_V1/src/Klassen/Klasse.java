package Klassen;

import Items.Item;
import Monsters.Dragon;
import Monsters.Goblin;
import Monsters.Monster;
import Monsters.Orc;
import Superclassess.AliveObj;
import View.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Sander on 13-3-2016.
 */
public class Klasse extends AliveObj{
    private int hitpoints;
    private int maxHitpoints;
    private int day = 1;
    private int damage;
    private int level;
    private int speed;
    private ArrayList<Item> inventory = new ArrayList<>();
    Random dice;
    private String name;
    private View view;
    private int input;
    private boolean waitForInput = true;
    private long exp = 0;
    private long expRequired = 4000;
    private boolean inFight;
    private Item equippedItem;
    private ArrayList<Monster> knownMonsters = new ArrayList<>();
    private boolean story1Complete = false;

    public Klasse() {
    }

    public Klasse(int hitpoints, int damage, Item item, int speed, View view) {
        this.hitpoints = hitpoints;
        this.maxHitpoints = hitpoints;
        this.damage = damage;
        this.level = 1;
        this.equippedItem = item;
//        this.inventory.add(item);
        this.speed = speed;
        this.view = view;
        this.dice = new Random();
        this.name = "default name";
        knownMonsters.add(new Dragon());
        knownMonsters.add(new Goblin());
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
        view.addFastString("Tracking " + m + "s...");
        int i = dice.nextInt(100);
        if(i<75){
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
        view.addString("You have discovered: " + m);
        getKnownMonsters().add(m);
        return m;
    }

    private void sleep(){
        view.addFastString("Getting some rest...");
        view.clearText();
        hitpoints = maxHitpoints;
        day++;
        view.addString("What a beautiful morning!");
    }

    public ArrayList<Monster> getKnownMonsters() {
        return knownMonsters;
    }

    public void showOptions(){
        view.addString("Type 1 to go to sleep");
        int i = 2;
        for(Monster m:getKnownMonsters()){
            view.addString("Type " + i + " to track " + m + "s");
            i++;
        }
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

    public int getDamage(){
        int i = dice.nextInt(100);
        if (i<equippedItem.getCritChance()){
            view.addString("Crit!");
            return (int)(damage*1.5);
        }
        return damage;
    }

    public void setInFight(boolean inFight) {
        this.inFight = inFight;
    }

    public void encounters(Monster m){
        m.initMonster();
        view.addString("You've encounterd a level " + m.getLevel() + " " + m.getName());
        setInFight(true);
        while(inFight) {
            view.addString("The " + m + " has " + m.getHitpoints() + " hitpoints.");
            view.addString("What do you want to do? 1. fight 2. flee");
            waitForUserInput();

            int n = getInput();
            switch (n) {
                case 1:
                    fight(m);
                    break;
                case 2:
                    if(flee(m)) {
                        inFight = false;
                    }
                    break;
            }
        }
    }

    private void checkLevelUp(){
        if(exp >= expRequired){
            this.level++;
            view.addString("Congratulations! You've gained a level!");
            view.addString("You are now level: " + this.level);
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
                view.addString("You defeated the " + a + "!");
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

    private void gameover(){
        view.addString("You did not achieve to kill the dragon sucker go get yourself a drink and play again");
        waitForUserInput();
        System.exit(0);
    }

    private boolean flee(Monster m){
        int x = dice.nextInt(100);
        if (x < m.getFlee()){
            view.addString("Success!");
            return true;
        } else {
            view.addString("Fail!");
            return false;
        }
    }

    public String toString(){
        return name;
    }
    public void setInput(int key){
        this.input = key;
        changed();
    }

    public int getInput() {
        return input;
    }

    public void changed(){
        this.setChanged();
        this.notifyObservers();
    }
    public void readInput(){
        waitForInput = false;
        if(getInput() == 9){
            System.exit(0);
        }
        if(getInput() == 0){
            String s="You have a ";
            for(Item i:getInventory()){
                s += i + ", ";
            }
            if(equippedItem!=null){
                view.addString("Equipped item: "+ equippedItem);
            }
            if (getInventory().isEmpty()) {
                view.addFastString("Your inventory is empty");
            } else {
                view.addFastString(s);
            }
        }
    }

    public boolean isWaitForInput() {
        return waitForInput;
    }

    public void setWaitForInput(boolean waitForInput) {
        this.waitForInput = waitForInput;
    }

    public void waitForUserInput(){
        setWaitForInput(true);
        while(isWaitForInput() || getInput() == 0 || getInput() == 9){
            setWaitForInput(true);
            try{
                Thread.sleep(1000);
            } catch(Exception e){
                e.printStackTrace();
            }

        }
        setWaitForInput(true);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void linkView(View view){
        this.view = view;
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

    public boolean isStory1Complete() {
        return story1Complete;
    }
}
