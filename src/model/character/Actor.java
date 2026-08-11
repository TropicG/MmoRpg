package model.character;

public abstract class Actor {

    protected int totalHealth;
    protected int currentHealth;

    protected int totalMana;
    protected int currentMana;

    protected int damage;
    protected int defense;
    protected int currentLevel;

    protected final int xpReward;

    protected int xPos;
    protected int yPos;

    public Actor(int health, int mana, int damage, int defense, int currentLevel, int xPos, int yPos, int xpReward) {
        this.totalHealth = health;
        this.currentHealth = health;

        this.totalMana = mana;
        this.currentMana = mana;

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

        int trueDamage = dmgPoints - defense;
        if (trueDamage < 0) {
            trueDamage = 0;
        }

        currentHealth -= trueDamage; //(dmgPoints - defense);

        System.out.println(dmgPoints - defense);

        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public boolean isDead() {
        return currentHealth == 0;
    }

    // GETTERS
    public int getTotalHealth() {
        return totalHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getTotalMana() {
        return totalMana;
    }

    public int getCurrentMana() {
        return currentMana;
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

    public int getXpReward() {
        return xpReward;
    }

    // SETTERS
    public void setXpos(int xPos) {
        this.xPos = xPos;
    }

    public void setYpos(int yPos) {
        this.yPos = yPos;
    }
}
