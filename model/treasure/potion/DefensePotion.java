package model.treasure.potion;

import model.treasure.TreasureFactory;

public class DefensePotion extends Potion {
    private static final int DEFENSE_POINTS = 5;

    public DefensePotion() {
        super(DEFENSE_POINTS, TreasureFactory.DEFENSE_POTION);
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", defenseBonus: " + super.amplifierPoints + "\n";
    }
}
