package model.treasure.potion;

import model.treasure.TreasureFactory;

public class AttackPotion extends Potion {
    private static final int ATTACK_POINTS = 5;

    public AttackPotion() {
        super(ATTACK_POINTS, TreasureFactory.ATTACK_POTION);
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", attackBonus: " + super.amplifierPoints + "\n";
    }
}
