package model.treasure.potion;

import model.treasure.TreasureFactory;

public class HealthPotion extends Potion {
    private static final int HEALTH_POINTS = 10;

    public HealthPotion() {
        super(HEALTH_POINTS, TreasureFactory.HEALTH_POTION);
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", healthBonus: " + super.amplifierPoints + "\n";
    }
}
