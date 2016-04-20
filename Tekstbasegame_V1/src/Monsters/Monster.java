package Monsters;

import Superclassess.AliveObj;

import java.util.Random;

/**
 * Created by Sander on 13-3-2016.
 */
public class Monster extends AliveObj{
    private String name;

    private int exp;
    private int damage;
    private int hitpoints;
    private int level;
    private int flee;
    private int speed;
    private int minLevel;
    private int maxLevel;

    private int currentExp;
    private int currentDamage;
    private int currentSpeed;
    private int currentHitpoints;

    private Random dice;

    public Monster(String name, int exp, int damage, int hitpoints, int flee, int speed, int minLevel, int maxLevel) {
        this.name = name;
        this.exp = exp;
        this.damage = damage;
        this.hitpoints = hitpoints;
        this.flee = flee;
        this.speed = speed;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;

        dice = new Random();
    }

    public void initMonster(){
        this.level = dice.nextInt(maxLevel-minLevel)+minLevel;
        currentExp=exp+level*1000;
        currentDamage=damage+level;
        currentSpeed= speed+level;
        currentHitpoints = hitpoints+level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getFlee() {
        return flee;
    }

    public String toString(){
        return name;
    }

    public int getHitpoints() {
        return currentHitpoints;
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public void subtractHitpoints(int x){
        this.currentHitpoints -= x;
    }

    public boolean isAlive(){
        return currentHitpoints > 0;
    }

    public int getDamage() {
        return currentDamage;
    }

    public int getExp() {
        return currentExp;
    }
}
