package model.treasure.potion;

public class AttackPotion extends Potion {
    private static final int ATTACK_POINTS = 5;

    public AttackPotion() {
        super(ATTACK_POINTS, "AttackPotion");
    }

    @Override
    public String toString() {
        return "Name: " + super.treasureName + ", attackBonus: " + super.amplifierPoints + "\n";
    }
}
