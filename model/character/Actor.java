package model.character;

public abstract class Actor {

    protected int health;
    protected int mana;
    protected int damage;
    protected int defense;
    protected int currentLevel;

    protected final int xpReward;

    protected int xPos;
    protected int yPos;

    //TODO: Change the names of the parameters for player location
    public Actor(int health, int mana, int damage, int defense, int currentLevel, int xPos, int yPos, int xpReward) {
        this.health = health;
        this.mana = mana;
        this.damage = damage;
        this.defense = defense;
        this.currentLevel = currentLevel;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xpReward = xpReward;
    }

    //BEHAVIOUR
    public int calculateAtkDmg() {
        return damage;
    }

    public void takeDamage(int dmgPoints) {
        health -= dmgPoints;

        if(health < 0) {
            health = 0;
        }
    }

    public boolean isDead() {
        return health == 0;
    }

    // GETTERS
    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    // SETTERS
    public void setXpos(int xPos) {
        this.xPos = xPos;
    }

    public void setYpos(int yPos) {
        this.yPos = yPos;
    }
}
