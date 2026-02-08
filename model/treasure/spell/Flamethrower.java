package model.treasure.spell;

public class Flamethrower extends Spell{
    private static final int ATTACK_DMG = 5;
    private static final int LEVEL_REQ = 2;
    private static final int MANA_COST = 12;

    public Flamethrower() {
        super(ATTACK_DMG, LEVEL_REQ, MANA_COST, "Flamethrower");
    }
}
