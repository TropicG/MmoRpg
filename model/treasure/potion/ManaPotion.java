package model.treasure.potion;

public class ManaPotion extends Potion {
    private static final int MANA_POINTS = 10;

    public ManaPotion() {
        super(MANA_POINTS, "ManaPotion");
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", manaBonus: " + super.amplifierPoints + "\n";
    }
}
