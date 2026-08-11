package model.treasure.spell;

import model.treasure.TreasureFactory;

public class HydroCannon extends Spell {
    private static final int ATTACK_DMG = 5;
    private static final int LEVEL_REQ = 1;
    private static final int MANA_COST = 5;

    public HydroCannon() {
        super(ATTACK_DMG, LEVEL_REQ, MANA_COST, TreasureFactory.HYDRO_CANNON);
    }
}
