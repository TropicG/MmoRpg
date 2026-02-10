package model.character.enemy;

import model.character.Actor;

public class Minion extends Actor {
    private static final int ATTACK_MODIFIER = 3;

    public Minion(int health, int mana, int damage, int defense, int currentLevel, int x, int y, int rewardXp) {
        super(health, mana, damage, defense, currentLevel, x, y, rewardXp);
    }

    @Override
    public int calculateAtkDmg() {
        return super.damage + (super.currentLevel * ATTACK_MODIFIER);
    }

    @Override
    public String toString() {
        return "HP: " + super.getCurrentHealth() + "/" + super.getTotalHealth() + ", " +
                "Dmg: " + super.getDamage() + ", " +
                "Def: " + super.getDefense() + ". " +
                "Lvl: " + super.currentLevel +
                "\n";
    }
}
