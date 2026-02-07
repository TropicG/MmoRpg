package model.character;

public abstract class Actor {

    private int health;
    private int mana;
    private int damage;
    private int defense;
    private int currentLevel;
    private int xPos;
    private int yPos;

    //TODO: Change the names of the parameters for player location
    public Actor(int health, int mana, int damage, int defense, int currentLevel, int xPos, int yPos) {
        this.health = health;
        this.mana = mana;
        this.damage = damage;
        this.defense = defense;
        this.currentLevel = currentLevel;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setXpos(int xPos) {
        this.xPos = xPos;
    }

    public void setYpos(int yPos) {
        this.yPos = yPos;
    }
}
