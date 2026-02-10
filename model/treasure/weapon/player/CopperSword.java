package model.treasure.weapon.player;

import model.treasure.TreasureFactory;
import model.treasure.weapon.Weapon;

public final class CopperSword extends Weapon {
    public static final int ATTACK_DMG = 3;
    public static final int LEVEL_REQ = 1;

    public CopperSword() {
        super(ATTACK_DMG, LEVEL_REQ, TreasureFactory.COPPER_SWORD);
    }
}
