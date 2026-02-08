package model.treasure.spell;

public class Twister extends Spell {
    private static final int ATTACK_DMG = 3;
    private static final int LEVEL_REQ = 1;
    private static final int MANA_COST = 10;

    public Twister() {
        super(ATTACK_DMG, LEVEL_REQ, MANA_COST, "Twister");
    }
}
