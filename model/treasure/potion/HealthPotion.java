package model.treasure.potion;

public class HealthPotion extends Potion {
    private static final int HEALTH_POINTS = 10;

    public HealthPotion() {
        super(HEALTH_POINTS, "HealthPotion");
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", healthBonus: " + super.amplifierPoints + "\n";
    }
}
