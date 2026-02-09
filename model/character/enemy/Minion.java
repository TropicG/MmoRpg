package model.character.enemy;

import model.character.Actor;

public class Minion extends Actor {
    private static final int ATTACK_MODIFIER = 3;

    //TODO: Change the names of the parameters for the Minion
    public Minion(int health, int mana, int damage, int defense, int currentLevel, int x, int y, int rewardXp) {
        super(health, mana, damage, defense, currentLevel, x, y, rewardXp);
    }

    @Override
    public int calculateAtkDmg() {
        return super.damage + (super.currentLevel * ATTACK_MODIFIER);
    }

    @Override
    public String toString() {
        StringBuilder minionTest = new StringBuilder();

        minionTest.append("HP: ").append(super.getCurrentHealth()).append("/").append(super.getTotalHealth()).append(", ");
        minionTest.append("Dmg: ").append(super.getDamage()).append(", ");
        minionTest.append("Def: ").append(super.getDefense()).append(". ");
        minionTest.append("Lvl: ").append(super.currentLevel);

        minionTest.append("\n");
        return minionTest.toString();
    }
}
