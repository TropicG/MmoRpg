package model.treasure.spell;

public class Thunderbolt extends Spell {
    private static final int ATTACK_DMG = 6;
    private static final int LEVEL_REQ = 2;
    private static final int MANA_COST = 12;

    public Thunderbolt() {
        super(ATTACK_DMG, LEVEL_REQ, MANA_COST, "Thunderbolt");
    }
}
