package Klassen;

import Items.Item;
import Main.TextBasedGame;
import Monsters.Dragon;
import Monsters.Monster1;
import Monsters.Monster;
import Monsters.Monster2;
import Superclassess.AliveObj;

import java.util.ArrayList;
import java.util.Random;

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
    //1 is warrior, 2 is mage, 3 is rogue, 0 is not chosen yet
    private int classChosen;

    private Monster currentFightMonster;

    private ArrayList<Monster> knownMonsters = new ArrayList<>();
    private ArrayList<Item> inventory = new ArrayList<>();
    private ArrayList<String> text = new ArrayList<>();

    private Random dice;
    private String name;

    private char input=' ';
    private boolean waitForInput = true;

    private TextBasedGame game;

    public Klasse() {
        this.classChosen = 0;
        this.exp = 0;
        this.expRequired = EXP_LEVEL_1;
        this.day = 1;
        this.level = 1;
        this.dice = new Random();
        this.name = "default name";
        this.knownMonsters.add(new Monster1());
        this.knownMonsters.add(new Dragon());
    }

    public void initClass(int hitpoints, int damage, int speed, Item startItem, int classChosen){
        this.hitpoints = hitpoints;
        this.maxHitpoints = hitpoints;
        this.damage = damage;
        this.equippedItem = startItem;
        this.speed = speed;
        this.classChosen = classChosen;
    }

    public void linkGame(TextBasedGame game){
        this.game = game;
    }


    //<editor-fold desc="Level up system">
    private void increaseStats(){
        this.damage+=level+dice.nextInt(level)*2;
        this.maxHitpoints+=level+dice.nextInt(level)*2;
        //this.hitpoints = maxHitpoints;
        this.speed+=dice.nextInt(level);
    }

    private void checkLevelUp(){
        while(exp >= expRequired){
            this.level++;
            addString("Congratulations! You've gained a level!");
            addString("You are now level: " + this.level);
            expRequired = (expRequired*(190+dice.nextInt(100)+1))/100;
            increaseStats();
        }
    }

    private void increaseExp(int e){
        this.exp += e;
        checkLevelUp();
    }
    //</editor-fold>

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }


    private void track(Monster m){
        addFastString("Tracking " + m + "s...");
        int i = dice.nextInt(1000);
        //TODO make a track change for a monster dependant of the user level
        System.out.println("roll: "  +i);
        System.out.println("change of finding: " + (898-level*100+200*getKnownMonsters().size()-1));

        //TODO make this dependant on the monster that your tracking
        if(isBetween(i,0,(898-level*100+200*getKnownMonsters().size()-1))){
            encounters(m);
        }
        else if (isBetween(i,998,1000)){
            encounters(new Dragon());
        }
        else {
            encounters(discoverMonster());
        }
    }

    private Monster discoverMonster(){
        //TODO discover algorithm
        Monster m = new Monster2();
        addString("You have discovered: " + m);
        getKnownMonsters().add(getKnownMonsters().size()-1,m);
        return m;
    }

    public void encounters(Monster m){
        setInFight(true);
        currentFightMonster = m.initMonster();
        addString("You've encounterd a level " + currentFightMonster.getLevel() + " " + currentFightMonster.getName());
        while(inFight) {
            addString("The " + m + " has " + m.getHitpoints() + " hitpoints.");
            addString("What do you want to do?");
            addString("1. fight");
            addString("2. flee");
            game.waitForUserInput(2);
            int n = getInput();
            switch (n) {
                case 1:
                    fight(m);
                    break;
                case 2:
                    if(flee(m)) {
                        //flee was succesfull
                        inFight = false;
                    }
                    break;
            }
        }
    }

    private void fight(Monster m){
        boolean b = false;
        if (classChosen==2) {
            addString("you can choose to use a spell");
            addString("type 1 for fireball");
            addString("type 2 to do nothing");
            int n = game.waitForUserInput(2);
            if(n==1){
                this.damage+=10;
                b = true;
            }
        }
        if(m.getSpeed() < this.getSpeed()){
            hit(this,m);
        } else{
            hit(m,this);
        }
        if (b){
            this.damage-=10;
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
        if (getSpeed()+dice.nextInt(getSpeed()) > m.getSpeed()+dice.nextInt(m.getSpeed())){
            addString("Success!");
            return true;
        } else {
            addString("Fail!");
            this.subtractHitpoints(m.getDamage());
            processDeath(this);
            return false;
        }
    }

    //<editor-fold desc="Daytime options handling">
    private void sleep(){
        addFastString("Getting some rest...");
        game.wait(500);
        clearText();
        hitpoints = maxHitpoints;
        day++;
        addString("What a beautiful morning!");
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

    public int showOptions(){
        addString("Type 1 to go to sleep");
        int i = 2;
        for(Monster m:getKnownMonsters()){
            addString("Type " + i + " to track " + m + "s");
            i++;
        }
        return i;
    }
    //</editor-fold>

    //<editor-fold desc="input handling">
    public void exitGame(){
        System.exit(0);
    }

    public void resetGame(){
        game.resetGame();
    }

    public void showInventory(){
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

    public void showStats(){
        if (classChosen!=0) {
            addFastString("Your damage is: " + getDamage() + " , speed is: " + getSpeed());
            if (inFight) {
                addString("The " + currentFightMonster + " damage is: "
                        + currentFightMonster.getDamage() + " , speed is: " + currentFightMonster.getSpeed()
                        + " , exp: " + currentFightMonster.getExp());
            }
        } else {
            addFastString("No stats yet!");
        }
    }

    public void gameover(){
        addString("You did not achieve to kill the dragon sucker go get yourself a drink and play again");
        game.waitForUserInput();
        game.resetGame();
    }



    //</editor-fold>

    //<editor-fold desc="setters and getters">

    public int getDamage(){
        return damage;
    }

    public void setInFight(boolean inFight) {
        this.inFight = inFight;
    }

    public int getInput() {
        return (int) input - 48;
    }

    public String getInputString(){
        return "" + input;
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

    public ArrayList<String> getText(){
        ArrayList<String> copy = text;
        return copy;
    }

    public void setInput(char key){
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
    //</editor-fold>

    //<editor-fold desc="Text management">
    public void addFastString(String s){
        if(!game.isInterrupted()) {
            if (getText().size() > 25) {
                text.remove(0);
            }
            text.add(s);
            changed();
        }
    }

    public void addString(String s){
        if(!game.isInterrupted()){
            game.wait(500);
        }
        addFastString(s);
    }

    public void clearText(){
        this.text.clear();
    }
    //</editor-fold>

    public void changed(){
        this.setChanged();
        this.notifyObservers();
    }

}
