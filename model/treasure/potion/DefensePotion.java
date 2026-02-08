package model.treasure.potion;

public class DefensePotion extends Potion {
    private static final int DEFENSE_POINTS = 5;

    public DefensePotion() {
        super(DEFENSE_POINTS, "DefensePotion");
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", defenseBonus: " + super.amplifierPoints + "\n";
    }
}
