package model.treasure.potion;

import model.treasure.TreasureFactory;

public class ManaPotion extends Potion {
    public static final int MANA_POINTS = 10;

    public ManaPotion() {
        super(MANA_POINTS, TreasureFactory.MANA_POTION);
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", manaBonus: " + super.amplifierPoints + "\n";
    }
}
