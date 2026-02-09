package model.character.enemy;

import model.character.Actor;

public class Minion extends Actor {
    //TODO: Change the names of the parameters for the Minion
    public Minion(int health, int mana, int damage, int defense, int currentLevel, int x, int y, int rewardXp) {
        super(health, mana, damage, defense, currentLevel, x, y, rewardXp);
    }

    @Override
    public int calculateAtkDmg() {
        return super.damage + super.currentLevel;
    }
}
